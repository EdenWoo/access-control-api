package com.cfgglobal.test.domain;

import com.cfgglobal.test.enums.AttachmentType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicUpdate
@DynamicInsert
public class Attachment extends BaseEntity {

    @NotNull
    String name;

    @NotNull
    String contentType;

    @NotNull
    Long size;

    @NotNull
    String originalFilename;

    String notes;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    AttachmentType type;

    @NotNull
    String fullPath;
}
