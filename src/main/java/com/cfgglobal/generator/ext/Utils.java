package com.cfgglobal.generator.ext;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public String hyphen(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source);
    }

    /**
     * e.g., "lower-hyphen".
     */
    public String lowerHyphen(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source);
    }


    /**
     * e.g., "lower_underscore".
     */
    public String lowerUnderscore(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(source);
    }

    /**
     * e.g., "lowerCamel".
     */
    public String lowerCamel(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(source);
    }

    /**
     * e.g., "UpperCamel".
     */
    public String upperCamel(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL).convert(source);
    }

    /**
     * e.g., "UPPER_UNDERSCORE".
     */
    public String upperUderscore(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert(source);
    }

}
