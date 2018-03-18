package com.github.leon.aci.security

import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * Created by fan.jin on 2017-04-04.
 */

class AnonAuthentication : AbstractAuthenticationToken(null) {

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return 7
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        return if (javaClass != obj.javaClass) {
            false
        } else true
    }


}
