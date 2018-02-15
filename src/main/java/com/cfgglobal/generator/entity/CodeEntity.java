package com.cfgglobal.generator.entity;

import com.cfgglobal.test.base.ClassSearcher;
import com.cfgglobal.test.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class CodeEntity {
    List<CodeField> fields;
    private Integer id;
    private String name;
    private String display;

}

