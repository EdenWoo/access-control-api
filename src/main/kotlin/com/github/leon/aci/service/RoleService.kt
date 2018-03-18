package com.github.leon.aci.service

import com.github.leon.aci.domain.Role
import com.github.leon.aci.service.base.BaseService
import org.springframework.stereotype.Service


@Service
class RoleService : BaseService<Role, Long>() {

    fun removeEmptyRules(role: Role): Role {
        val rolePermissions = role.rolePermissions
        role.rolePermissions.addAll(rolePermissions.filter { (_, rules) -> !rules.isEmpty() })
        return role

    }
}
