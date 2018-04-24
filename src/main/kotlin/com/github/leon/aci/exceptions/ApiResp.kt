package com.github.leon.aci.exceptions



data class ApiResp (
    val code: Int? = null,
    var message: String? = null,
    var error: String? = null
)
