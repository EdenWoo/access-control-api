package com.github.leon.setting.domain

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.sms.enums.SmsProviderType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class Setting(
        val name: String,
        @Type(type = "yes_no")
        val active: Boolean,
        val smsProviderType: SmsProviderType

) : BaseEntity()