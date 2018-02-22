package com.cfgglobal.generator.ext

import com.google.common.base.CaseFormat
import lombok.experimental.UtilityClass

import java.util.regex.Matcher
import java.util.regex.Pattern

@UtilityClass
class Utils {
    companion object {

        fun hyphen(source: String): String {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source)
        }

        /**
         * e.g., "lower-hyphen".
         */
        fun lowerHyphen(source: String): String {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source)
        }


        /**
         * e.g., "lower_underscore".
         */
        fun lowerUnderscore(source: String): String {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(source)
        }

        /**
         * e.g., "lowerCamel".
         */
        fun lowerCamel(source: String): String {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(source)
        }

        /**
         * e.g., "UpperCamel".
         */
        fun upperCamel(source: String): String {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL).convert(source)
        }

        /**
         * e.g., "Spaced Capital".
         */
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
        fun upperUderscore(source: String): String {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert(source)
        }
    }

}
