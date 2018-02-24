package com.cfgglobal.test.domain


import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class VisitRecord(
        val ip: String = "",
        val uri: String = "",
        val method: String = "",
        @Column(length = 1024)
        val queryString: String? = null,
        @Column(length = 2048)
        val requestBody: String? = null,
        val executionTime: Long = -1
) : BaseEntity()