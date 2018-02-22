package com.cfgglobal.test.web.api

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.domain.Attachment
import com.cfgglobal.test.enums.AttachmentType
import com.cfgglobal.test.service.AttachmentService
import com.github.leon.ams.s3.AmazonService
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import java.io.IOException
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
        response.setHeader("Content-Disposition", "inline; filename=" + filename)
        try {
            val file = amazonService!!.getFile(filename)
            val `in` = FileInputStream(file)
            IOUtils.copy(`in`, response.outputStream)
            response.flushBuffer()
        } catch (ex: IOException) {
            throw RuntimeException("IOError writing file to output stream")
        }


    }

    @PostMapping
    @ResponseBody
    fun create(file: MultipartFile, type: AttachmentType?): ResponseEntity<*> {

        return attachmentService!!
                .createFile(file, type.toOption().getOrElse { AttachmentType.CUSTOMER_BANK_ACCOUNT_DOC })
                .fold(
                        { ResponseEntity.ok(it) },
                        { attachment ->
                            attachmentService.save<Attachment>(attachment)
                            ResponseEntity.ok<Attachment>(attachment)
                        }
                )
    }
}
