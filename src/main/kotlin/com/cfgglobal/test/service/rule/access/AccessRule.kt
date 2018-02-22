package com.cfgglobal.test.service.rule.access

import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.vo.Filter

interface AccessRule {

    val ruleName: String

    fun exec(permission: Permission): Filter
}
