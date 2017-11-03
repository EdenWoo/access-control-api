package com.cfgglobal.test.domain;

import com.cfgglobal.test.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailLog extends BaseEntity implements Serializable {

    String subject;
    @Lob
    @Column(length = 100000)
    @JsonIgnore
    byte[] content;
    String sendTo;
    @Enumerated(value = EnumType.STRING)
    TaskStatus status;
    Integer times;//发送次数
    String msg;
}
