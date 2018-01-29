package com.cfgglobal.generator.entity;

import lombok.Data;


@Data
public class CodeField {
    private Integer id;
    private String name;
    private String display;
    private Integer length;
    private Integer scale;
    private String type;
    private boolean isPrimaryKey;
    private boolean isSearchable;
    private boolean isSortable;
    private boolean isRequired;
    private boolean isUnique;
}
