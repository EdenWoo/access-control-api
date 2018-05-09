package com.github.leon.setting.domain

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.email.domain.EmailServer
import com.github.leon.generator.metadata.FieldFeature
import com.github.leon.sms.enums.SmsProviderType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import org.hibernate.validator.constraints.Range
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
@DynamicUpdate
@DynamicInsert
data class Setting(
        var name: String = "",
        val serverDomain: String = "",
        var customerLogo: String = "",
        var adminLogo: String = "",
        @Range(min = 0, max = 24)
        val startWorkHour: Int,
        @Range(min = 0, max = 24)
        val endWorkHour: Int,
        @Type(type = "yes_no")
        var active: Boolean? = null,
        var smsProviderType: SmsProviderType? = null,
        @FieldFeature(selectOne = true)
        @ManyToOne
        val emailServer: EmailServer
) : BaseEntity()