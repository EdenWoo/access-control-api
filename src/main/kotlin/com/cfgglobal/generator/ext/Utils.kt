package com.cfgglobal.generator.ext

import com.google.common.base.CaseFormat

class Utils {
    companion object {

        fun hyphen(source: String): String? {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source)
        }

        /**
         * e.g., "lower-hyphen".
         */
        fun lowerHyphen(source: String): String? {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(source)
        }


        /**
         * e.g., "lower_underscore".
         */
        fun lowerUnderscore(source: String): String? {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(source)
        }

        /**
         * e.g., "lowerCamel".
         */
        fun lowerCamel(source: String): String? {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(source)
        }

        /**
         * e.g., "UpperCamel".
         */
        fun upperCamel(source: String): String? {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL).convert(source)
        }

        /**
         * e.g., "UPPER_UNDERSCORE".
         */
        fun upperUderscore(source: String): String? {
            return CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert(source)
        }
    }

}
