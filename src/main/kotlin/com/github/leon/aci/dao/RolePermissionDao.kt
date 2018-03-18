package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.RolePermission
import org.springframework.stereotype.Repository

@Repository
interface RolePermissionDao : BaseDao<RolePermission, Long>