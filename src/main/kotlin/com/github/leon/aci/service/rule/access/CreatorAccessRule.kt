package com.github.leon.aci.service.rule.access


import com.github.leon.aci.domain.Permission
import com.github.leon.aci.vo.Filter
import org.springframework.stereotype.Component

@Deprecated("")
@Component
class CreatorAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "creator"

    override fun exec(permission: Permission): Filter {
        val user = securityFilter!!.currentUser()
        val orgFilter = Filter()
        orgFilter.addCondition("creator.id", user.id, Filter.OPERATOR_EQ)
        return orgFilter
    }
}
