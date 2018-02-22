package com.cfgglobal.test.service.rule

import arrow.syntax.collections.firstOption
import com.cfgglobal.test.domain.User
import com.cfgglobal.test.vo.Filter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.text.MessageFormat
import java.util.regex.Pattern

@Component
class SecurityFilterImpl : SecurityFilter {


    @Autowired
    private val ruleService: RuleService? = null

    override fun currentUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User

    }

    override fun query(method: String, requestURI: String): List<Filter> {

        val permission = currentUser().role!!
                .rolePermissions
                .map { it.permission }
                .firstOption {
                    it!!.authUris.split(";").any { uriPatten -> Pattern.matches(uriPatten, requestURI) }
                }
        return when (permission.isDefined()) {
            true -> {
                currentUser().role!!.rolePermissions.firstOption { it.permission!!.id == permission.get()!!.id }.map { it.rules }
                        .map {
                            it.map { ruleService!!.findAccessRules(it.name) }.map { it.get().exec(permission.get()!!) }
                        }.get()
            }
            false -> {
                throw AccessDeniedException(MessageFormat.format("No permission {0} {1}", method, requestURI))
            }
        }


    }
}
