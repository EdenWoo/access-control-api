package com.github.leon.generator.entity


data class CodeField(
        val name: String,
        val type: FieldType,
        val primaryKey: Boolean = false,
        val searchable: Boolean = false,
        val sortable: Boolean = false,
        val required: Boolean = false,
        val unique: Boolean = false,
        val hiddenInForm: Boolean = false,
        val hiddenInList: Boolean = false,
        val limit: Int? = null,
        val sizeMin: Int? = null,
        val sizeMax: Int? = null,
        val rangeMin: Long? = null,
        val rangeMax: Long? = null,
        val pattern: String? = null,
        val future: Boolean? = null,
        val past: Boolean? = null,
        val switch: Boolean? = false,
        val range: Boolean? = false,
        val attachment: Boolean? = false,
        val selectOne: Boolean? = false,
        val selectMany: Boolean? = false,
        val addDynamicMany: Boolean? = false,
        val textarea: Boolean = false,
        val richText: Boolean = false,
        val display: List<String> = listOf(),
        val order: Int = 0,
        val weight: Int = 6


)




