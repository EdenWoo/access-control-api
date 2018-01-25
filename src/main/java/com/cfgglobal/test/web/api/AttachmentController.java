package com.cfgglobal.test.web.api;

import com.cfgglobal.test.enums.AttachmentType;
import com.cfgglobal.test.service.AttachmentService;
import com.cfgglobal.test.util.aws.AmazonService;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
@RequestMapping("/v1/attachment")
public class AttachmentController {

    @Autowired
    private AmazonService amazonService;

    @Autowired
    private AttachmentService attachmentService;
    @GetMapping("/download")
    public void download(@RequestParam String filename, HttpServletResponse response) {
        response.setHeader("Content-Disposition", "inline; filename=" + filename);
        try {
            File file = amazonService.getFile(filename);
            InputStream in = new FileInputStream(file);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }


    }

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
