package com.cfgglobal.test.service.rule.access


import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.vo.Filter
import com.cfgglobal.test.vo.Filter.Companion.OPERATOR_EQ
import com.cfgglobal.test.vo.Filter.Companion.OPERATOR_IN

import org.springframework.stereotype.Component

@Component
class IbAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "ib"

    override fun exec(permission: Permission): Filter {
        val user = securityFilter!!.currentUser()
        val filter = Filter()
        when {
            permission.entity == "user" -> filter.addCondition("introducedBy.id", user.id, OPERATOR_EQ)
            permission.entity == "price-category" -> filter.addCondition("id", arrayOf(5, 6, 7, 9), OPERATOR_IN)
            else -> filter.addCondition("user.introducedBy.id", user.id, OPERATOR_EQ)
        }

        return filter
    }
}
