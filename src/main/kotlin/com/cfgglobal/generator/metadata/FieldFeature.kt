
package com.cfgglobal.generator.metadata


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldFeature(
        val sortable: Boolean = false,
        val searchable: Boolean = false,
        /**
         * 字符显示长度，剩下的用...省略
         */
        val limit:Int=255

)


