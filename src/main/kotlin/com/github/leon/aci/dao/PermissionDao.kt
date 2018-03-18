package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao

import com.github.leon.aci.domain.Permission
import org.springframework.stereotype.Repository

@Repository
interface PermissionDao : BaseDao<Permission, Long> {

    fun findByHttpMethod(httpMethod: String): Permission

    fun findByEntity(entity: String): List<Permission>
}