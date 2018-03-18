package com.github.leon.aci.service

import com.github.leon.aci.domain.Permission
import com.github.leon.aci.service.base.BaseService
import org.springframework.stereotype.Service

@Service
class PermissionService : BaseService<Permission, Long>()
