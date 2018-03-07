package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.User
import org.springframework.stereotype.Repository

@Repository
interface UserDao : BaseDao<User, Long> {

    fun findByUsername(name: String): User

}