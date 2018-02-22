package com.cfgglobal.test.service

import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.service.base.BaseService
import org.springframework.stereotype.Service

@Service
class PermissionService : BaseService<Permission, Long>()
