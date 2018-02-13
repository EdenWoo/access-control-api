package com.cfgglobal.generator.ext;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * e.g., "Spaced Capital".
     */
    public String spacedCapital(String source){
        String s = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL).convert(source);
        StringBuilder out = new StringBuilder(s);
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(s);
        int extraFeed = 0;
        while(m.find()){
            if(m.start()!=0){
                out = out.insert(m.start()+extraFeed, " ");
                extraFeed++;
            }
        }
//        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(source).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
        return out.toString();
    }

    /**
     * e.g., "UPPER_UNDERSCORE".
     */
    public String upperUderscore(String source){
        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert(source);
    }

}
