package com.github.leon.aci.web.api

import com.github.leon.aci.enums.AttachmentType
import com.github.leon.aci.enums.AttachmentType.CUSTOMER_BANK_ACCOUNT_DOC
import com.github.leon.aci.service.AttachmentService
import com.github.leon.aws.s3.AmazonService
import com.github.leon.extentions.orElse
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/v1/attachment")
class AttachmentController(
        @Autowired
        val amazonService: AmazonService,

        @Autowired
        val attachmentService: AttachmentService

) {

    @GetMapping("/download")
    fun download(@RequestParam filename: String, response: HttpServletResponse) {
        response.setHeader("Content-Disposition", "inline; filename=$filename")
        response.contentType  = "application/octet-stream"
        val file = amazonService.getFile(filename)
        IOUtils.copy(FileInputStream(file), response.outputStream)
        response.flushBuffer()
    }

    @PostMapping("/upload")
    @ResponseBody
    fun create(file: MultipartFile, type: AttachmentType?): ResponseEntity<*> {

        return attachmentService
                .createFile(file, type.orElse(CUSTOMER_BANK_ACCOUNT_DOC))
                .fold(
                        { ResponseEntity.ok(it) },
                        { attachment ->
                            attachmentService.save(attachment)
                            ResponseEntity.ok(attachment)
                        }
                )
    }
}
