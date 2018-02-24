package com.cfgglobal.generator.ext

import com.google.common.base.CaseFormat
import java.util.regex.Pattern


object Utils {
    @JvmStatic
    fun hyphen(source: String): String {
        return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source) as String
    }

    /**
     * e.g., "lower-hyphen".
     */
    @JvmStatic
    fun lowerHyphen(source: String): String {
        return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source) as String
    }


    /**
     * e.g., "lower_underscore".
     */
    @JvmStatic
    fun lowerUnderscore(source: String): String {
        return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(source) as String
    }

    /**
     * e.g., "lowerCamel".
     */
    @JvmStatic
    fun lowerCamel(source: String): String {
        return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(source) as String
    }

    /**
     * e.g., "UpperCamel".
     */
    @JvmStatic
    fun upperCamel(source: String): String {
        return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL).convert(source) as String
    }

    /**
     * e.g., "Spaced Capital".
     */
    @JvmStatic
    fun spacedCapital(source: String): String {
        val s = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL).convert(source)
        var out = StringBuilder(s)
        val p = Pattern.compile("[A-Z]")
        val m = p.matcher(s)
        var extraFeed = 0
        while (m.find()) {
            if (m.start() != 0) {
                out = out.insert(m.start() + extraFeed, " ")
                extraFeed++
            }
        }
        //        return  CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(source).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
        return out.toString()
    }

    /**
     * e.g., "UPPER_UNDERSCORE".
     */
    @JvmStatic
    fun upperUderscore(source: String): String {
        return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert(source) as String
    }


}
