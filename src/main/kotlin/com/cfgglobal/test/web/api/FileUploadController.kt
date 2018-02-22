package com.cfgglobal.test.web.api

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.enums.AttachmentType
import com.cfgglobal.test.service.AttachmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

@Deprecated("")
@Controller
@RequestMapping("/v1/file")
class FileUploadController {

    @Autowired
    private val attachmentService: AttachmentService? = null

    @PostMapping
    @ResponseBody
    fun create(file: MultipartFile, type: AttachmentType?): ResponseEntity<*> {
        return attachmentService!!
                .createFile(file, type.toOption().getOrElse { AttachmentType.CUSTOMER_BANK_ACCOUNT_DOC })
                .fold({ ResponseEntity.ok(it) }, { attachment ->
                    attachmentService.save(attachment)
                    attachment.creator = null
                    attachment.modifier = null
                    ResponseEntity.ok(attachment)
                })
    }
} 