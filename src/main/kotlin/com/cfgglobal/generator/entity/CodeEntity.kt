package com.cfgglobal.generator.entity


data class CodeEntity(
        var fields: List<CodeField> = listOf(),
        var id: Int? = null,

        var name: String,

        var display: String? = null


)

