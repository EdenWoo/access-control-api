package com.cfgglobal.test.config.jpa;

import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZonedDateTime;
import java.util.Optional;

@Configurable
public class JpaAuditingEntityListener {

    private AuditorAware<User> auditorAware = () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(e -> (User) e.getPrincipal());

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * persist events.
     *
     * @param target
     */
    @PrePersist
    public void touchForCreate(Object target) {

        Assert.notNull(target, "Entity must not be null!");

        BaseEntity baseEntity = (BaseEntity) target;
        auditorAware.getCurrentAuditor().ifPresent(baseEntity::setCreator);
        baseEntity.setCreatedAt(ZonedDateTime.now());

    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * update events.
     *
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(Object target) {

        BaseEntity baseEntity = (BaseEntity) target;
        auditorAware.getCurrentAuditor().ifPresent(baseEntity::setModifier);
        baseEntity.setUpdatedAt(ZonedDateTime.now());

    }
}
