
package com.github.leon.generator.metadata


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldFeature(
        val sortable: Boolean = false,
        val searchable: Boolean = false,
        val display: Boolean = true,
        val boolean: Boolean = true,
        val require: Boolean = true,
        val switch: Boolean = false,
        val attachment: Boolean = false,
        val selectOne: Boolean = false,
        val selectMany: Boolean = false,
        val addDynamicMany: Boolean = false,
        /**
         * 不在表单显示
         */
        val hiddenInForm: Boolean = false,
        /**
         * 不在列表显示
         */
        val hiddenInList: Boolean = false,
        /**
         * 字符显示长度，剩下的用...省略
         */
        val limit:Int=255

)


