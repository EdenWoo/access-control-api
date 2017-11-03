package com.cfgglobal.test.service;

import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.domain.Attachment;
import com.cfgglobal.test.enums.AttachmentType;
import com.cfgglobal.test.service.base.BaseService;
import com.cfgglobal.test.util.aws.UploadUtil;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@EnableConfigurationProperties(value = {ApplicationProperties.class})

public class AttachmentService extends BaseService<Attachment, Long> {


    @Autowired
    private ApplicationProperties appProp;

    @Autowired
    private UploadUtil uploadUtil;

    public Either<String, Attachment> createFile(MultipartFile tempFile, AttachmentType type) {
        if (tempFile.isEmpty()) {
            return Either.left("no file uploaded");
        }
        String name = uploadUtil.write(tempFile, "");
        Attachment attachment = new Attachment()
                .setOriginalFilename(tempFile.getOriginalFilename())
                .setName(name)
                .setSize(tempFile.getSize())
                .setContentType(tempFile.getContentType())
                .setType(type)
                .setFullPath("/v1/attachment/download?filename=" + name);
        save(attachment);
        return Either.right(attachment);
    }


}
