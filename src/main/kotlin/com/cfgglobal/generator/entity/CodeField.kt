package com.cfgglobal.generator.entity


data class CodeField(
        var id: Int? = null,

        var name: String,

        var display: String? = null,

        var length: Int? = null,

        var scale: Int? = null,

        var type: FieldType,

        var isPrimaryKey: Boolean = false,

        var searchable: Boolean = false,

        var sortable: Boolean = false,

        var required: Boolean = false,

        var isUnique: Boolean = false

)
