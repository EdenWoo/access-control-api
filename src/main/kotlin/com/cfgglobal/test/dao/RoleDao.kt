package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.Role
import io.vavr.control.Option
import org.springframework.stereotype.Repository

@Repository
interface RoleDao : BaseDao<Role, Long> {

    fun findByName(roleName: String): Option<Role>
}