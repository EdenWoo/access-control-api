package com.cfgglobal.test.base;

import lombok.Data;

@Data
public class ApiResp {
    private Integer code;

    private String message;

    private String error;

}

