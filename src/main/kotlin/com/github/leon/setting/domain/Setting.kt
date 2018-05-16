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
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ManyToOne

@Entity
@DynamicUpdate
@DynamicInsert
data class Setting(
        var name: String = "",
        val serverDomain: String = "",
        var customerLogo: String = "",
        var adminLogo: String = "",
        var copyright: String = "",
        @Range(min = 0, max = 24)
        val startWorkHour: Int = 0,
        @Range(min = 0, max = 24)
        var endWorkHour: Int = 24,
        @Type(type = "yes_no")
        var active: Boolean? = null,
        @Enumerated(EnumType.STRING)
        var smsProviderType: SmsProviderType? = null,
        @FieldFeature(selectOne = true)
        @ManyToOne
        var emailServer: EmailServer? = null,
        var requestThreshold:Int = 500,
        var ipBlackList: String? = null,
        var ipWhiteList: String? = null
) : BaseEntity()