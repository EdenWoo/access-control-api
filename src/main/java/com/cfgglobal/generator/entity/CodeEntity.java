package com.cfgglobal.generator.entity;

import com.cfgglobal.test.base.ClassSearcher;
import com.cfgglobal.test.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class CodeEntity {
    List<CodeField> codeFields;
    private Integer id;
    private String name;
    private String display;

    public static void main(String[] args) {
        java.util.List<Class<?>> classes = ClassSearcher.of(BaseEntity.class).search();
        System.out.println(classes);

    }
}

