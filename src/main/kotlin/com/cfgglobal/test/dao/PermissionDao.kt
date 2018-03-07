package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao

import com.cfgglobal.test.domain.Permission
import org.springframework.stereotype.Repository

@Repository
interface PermissionDao : BaseDao<Permission, Long> {

    fun findByHttpMethod(httpMethod: String): Permission

    fun findByEntity(entity: String): List<Permission>
}