package com.github.leon.aci.service.rule.access


import com.github.leon.aci.domain.Permission
import com.github.leon.aci.vo.Condition
import com.github.leon.aci.vo.Filter
import org.springframework.stereotype.Component

@Deprecated("")
@Component
class CreatorAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "creator"

    override fun exec(permission: Permission): Filter {
        val user = securityFilter!!.currentUser()
        return Filter(
                conditions = listOf(
                        Condition(
                                fieldName =  "creator.id",
                                value = user.id,
                                operator =  Filter.OPERATOR_EQ)
                )
        )
    }
}
