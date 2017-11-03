package com.cfgglobal.test.web.api.vo;

import com.cfgglobal.test.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailLogVo {

    String subject;
    @Lob
    @Column(length = 100000)
    byte[] content;
    String html;
    String sendTo;
    @Enumerated(value = EnumType.STRING)
    TaskStatus status;
    String msg;
}
