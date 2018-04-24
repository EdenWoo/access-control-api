package com.github.leon.aci.service

import com.github.leon.aci.domain.Role
import com.github.leon.aci.service.base.BaseService
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils.isEmpty


@Service
class RoleService : BaseService<Role, Long>() {

    fun removeEmptyRules(role: Role): Role {
        val rolePermissions = role.rolePermissions
        role.rolePermissions += rolePermissions.filter { isEmpty(it.rules).not() }
        return role
    }
}
