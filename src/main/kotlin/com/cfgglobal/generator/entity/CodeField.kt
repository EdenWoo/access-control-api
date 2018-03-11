package com.cfgglobal.generator.entity


data class CodeField(
        val name: String,
        val display: String? = null,
        val type: FieldType,
        val primaryKey: Boolean = false,
        val searchable: Boolean = false,
        val sortable: Boolean = false,
        val required: Boolean = false,
        val unique: Boolean = false,
        val hidden: Boolean = false,
        val limit:Int = 255,
        val min: Long? = null,
        val max: Long? = null,
        val size: Size? = null,
        val switch: Boolean? = false,
        val attachment: Boolean? = false,
        val selectOne: Boolean? = false,
        val selectMany: Boolean? = false,
        val addDynamicMany: Boolean? = false


)

data class Size(
        val min: Int? = null,
        val max: Int? = null
)



