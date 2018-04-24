package com.github.leon.sms.domain

import com.github.leon.aci.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class MessageTemplate(
        var name: String="",
        var template: String="",
        var notes: String=""

) : BaseEntity()