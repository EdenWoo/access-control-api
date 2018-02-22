package com.cfgglobal.test.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.*


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)

abstract class BaseEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Version
        var version: Long? = null,

        @CreatedDate
        @Column(columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)", insertable = false, updatable = false)
        var createdAt: ZonedDateTime? = null,

        @LastModifiedDate
        @Column(columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)", insertable = false, updatable = false)
        var updatedAt: ZonedDateTime? = null,

        @CreatedBy
        @ManyToOne
        @JoinColumn(name = "creator_id")
        var creator: User? = null,

        @LastModifiedBy
        @ManyToOne
        @JoinColumn(name = "modifier_id")
        var modifier: User? = null


) : Serializable {
    companion object {
        protected const val serialVersionUID = 1L
    }
}
