package com.cfgglobal.test.service.rule.access


import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.vo.Filter
import org.springframework.stereotype.Component

@Component
class UserAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "user"

    override fun exec(permission: Permission): Filter {
        val user = securityFilter!!.currentUser()
        val orgFilter = Filter()
        orgFilter.addCondition("user.id", user.id, Filter.OPERATOR_GREATER_EQ)
        return orgFilter
    }
}
