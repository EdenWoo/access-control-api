package com.cfgglobal.generator.entity


data class CodeEntity(
        var fields: MutableList<CodeField> = mutableListOf(),
        var id: Int? = null,

        var name: String? = null,

        var display: String? = null


)

