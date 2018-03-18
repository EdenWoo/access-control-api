package com.github.leon.aci.config.jpa

import com.github.leon.aci.domain.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SecurityAuditor : AuditorAware<User> {

    override fun getCurrentAuditor(): User? {
        return Optional.ofNullable(SecurityContextHolder.getContext().authentication)
                .map { it.principal }
                .map { it as User }.orElse(null)

    }
}
