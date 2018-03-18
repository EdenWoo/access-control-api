package com.github.leon.aci.service.rule.access


import com.github.leon.aci.domain.Permission
import com.github.leon.aci.vo.Filter
import org.springframework.stereotype.Component

@Component
class AdminAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "admin"

    override fun exec(permission: Permission): Filter {
        return Filter.EMPTY
    }
}
