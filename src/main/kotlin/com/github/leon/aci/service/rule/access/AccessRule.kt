package com.github.leon.aci.service.rule.access

import com.github.leon.aci.domain.Permission
import com.github.leon.aci.vo.Filter

interface AccessRule {

    val ruleName: String

    fun exec(permission: Permission): Filter
}
