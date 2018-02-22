package com.cfgglobal.test.service.rule


import com.cfgglobal.test.domain.User
import com.cfgglobal.test.vo.Filter


interface SecurityFilter {

    fun currentUser(): User

    fun query(method: String, requestURI: String): List<Filter>

}
