package com.cfgglobal.test.config.jpa;

import com.cfgglobal.test.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditor implements AuditorAware<User> {

    @Override
    public User getCurrentAuditor() {
        return Optional.of(SecurityContextHolder.getContext().getAuthentication())
                .map(e -> (User) e.getPrincipal()).orElse(null);

    }
}
