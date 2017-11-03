package com.cfgglobal.test.web.api;

import com.cfgglobal.test.enums.AttachmentType;
import com.cfgglobal.test.service.AttachmentService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/v1/file")
public class FileUploadController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping
    @ResponseBody
    public ResponseEntity create(MultipartFile file, AttachmentType type) {
        type = Option.of(type).getOrElse(AttachmentType.CUSTOMER_BANK_ACCOUNT_DOC);
        return attachmentService
                .createFile(file, type)
                .fold(ResponseEntity::ok, attachment -> {
                    attachmentService.save(attachment);
                    attachment.setCreator(null);
                    attachment.setModifier(null);
                    return ResponseEntity.ok(attachment);
                });
    }
} 