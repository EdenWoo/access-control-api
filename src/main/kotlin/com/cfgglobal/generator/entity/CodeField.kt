package com.cfgglobal.generator.entity


data class CodeField(
        var id: Int? = null,

        var name: String? = null,

        var display: String? = null,

        var length: Int? = null,

        var scale: Int? = null,

        var type: String? = null,

        var isPrimaryKey: Boolean = false,

        var isSearchable: Boolean = false,

        var isSortable: Boolean = false,

        var isRequired: Boolean = false,

        var isUnique: Boolean = false

)
