package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.Role
import org.springframework.stereotype.Repository

@Repository
interface RoleDao : BaseDao<Role, Long> {

    fun findByName(roleName: String): Role
}