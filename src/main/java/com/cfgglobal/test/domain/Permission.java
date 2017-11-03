package com.cfgglobal.test.domain;


import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@Accessors
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {

    @NonNull
    String entity;

    @NonNull
    String authKey;

    @NonNull
    String httpMethod;

    @NonNull
    String authUris;


    String icon;

    String menuUrl;

}
