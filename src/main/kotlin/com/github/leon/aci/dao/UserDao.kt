package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.User
import org.springframework.stereotype.Repository

@Repository
interface UserDao : BaseDao<User, Long> {

    fun findByUsername(name: String): User

}