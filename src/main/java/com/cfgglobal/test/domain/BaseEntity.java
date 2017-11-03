package com.cfgglobal.test.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;


@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    @Version
    protected Long version;

    @CreatedDate
    @Column(columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)", insertable = false, updatable = false)
    protected ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)", insertable = false, updatable = false)
    protected ZonedDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "creator_id")
    @CreatedBy
    protected User creator;

    @ManyToOne
    @JoinColumn(name = "modifier_id")
    @LastModifiedBy
    protected User modifier;


}
