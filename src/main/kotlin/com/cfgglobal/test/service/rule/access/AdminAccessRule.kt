package com.cfgglobal.test.service.rule.access


import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.vo.Filter
import org.springframework.stereotype.Component

@Component
class AdminAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "admin"

    override fun exec(permission: Permission): Filter {
        return Filter.EMPTY
    }
}
