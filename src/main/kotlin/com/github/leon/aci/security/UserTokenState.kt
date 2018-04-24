package com.github.leon.aci.security


data class UserTokenState(
        var access_token: String? = null,
        var expires_in: Long? = null,
        var type: String? = null


)