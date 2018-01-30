package com.cfgglobal.generator.ext;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public String hyphen(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source);
    }

}
