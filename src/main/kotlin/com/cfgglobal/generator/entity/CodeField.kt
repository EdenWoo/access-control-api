package com.cfgglobal.generator.entity


data class CodeField(
        var id: Int? = null,

        var name: String,

        var length: Int? = null,

        var scale: Int? = null,

        var type: FieldType,

        var primaryKey: Boolean = false,

        var searchable: Boolean = false,

        var sortable: Boolean = false,

        var required: Boolean = false,

        var unique: Boolean = false,

        var display: Boolean? = true

)
