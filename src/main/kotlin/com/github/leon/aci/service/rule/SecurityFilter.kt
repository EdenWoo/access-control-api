package com.github.leon.aci.service.rule


import com.github.leon.aci.domain.User
import com.github.leon.aci.vo.Filter


interface SecurityFilter {

    fun currentUser(): User

    fun query(method: String, requestURI: String): List<Filter>

}
