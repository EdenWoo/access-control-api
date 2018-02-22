package com.cfgglobal.test.service

import com.cfgglobal.test.domain.Role
import com.cfgglobal.test.service.base.BaseService
import org.springframework.stereotype.Service


@Service
class RoleService : BaseService<Role, Long>() {

    fun removeEmptyRules(role: Role): Role {
        val rolePermissions = role.rolePermissions
        role.rolePermissions.addAll(rolePermissions.filter { (_, rules) -> !rules.isEmpty() })
        return role

    }
}
