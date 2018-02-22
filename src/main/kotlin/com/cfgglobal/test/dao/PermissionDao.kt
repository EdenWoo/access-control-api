package com.cfgglobal.test.dao

//import com.github.leon.security.BaseDao
import com.cfgglobal.test.dao.base.BaseDao

import com.cfgglobal.test.domain.Permission
import io.vavr.collection.List
import io.vavr.control.Option
import org.springframework.stereotype.Repository

@Repository
interface PermissionDao : BaseDao<Permission, Long> {

    fun findByHttpMethod(httpMethod: String): Option<Permission>

    fun findByEntity(entity: String): List<Permission>
}