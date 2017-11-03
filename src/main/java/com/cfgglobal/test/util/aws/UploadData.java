package com.cfgglobal.test.util.aws;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UploadData implements Serializable {

    private static final long serialVersionUID = -166575150661617870L;

    private String name;
    private byte[] data;
}
