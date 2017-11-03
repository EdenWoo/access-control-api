package com.cfgglobal.test.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SysConfig extends BaseEntity {
    String confKey;
    String confVal;
}
