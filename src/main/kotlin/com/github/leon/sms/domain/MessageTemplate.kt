package com.github.leon.sms.domain

import com.github.leon.aci.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class MessageTemplate(
        val name: String,
        val template: String,
        val notes: String

) : BaseEntity()