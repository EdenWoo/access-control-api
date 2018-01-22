package com.cfgglobal.test.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisitRecord extends BaseEntity {

    String ip;
    String uri;
    String queryString;
    String requestBody;
    Long executionTime;

}
