package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity

@DynamicUpdate
@DynamicInsert
data class CurrencyCountryConfig(

        @ManyToOne
        @JoinColumn(name = "currency_id")
        var currency: Currency? = null,

        @ManyToOne
        @JoinColumn(name = "country_id")
        var country: Country? = null,

        var clearingCodeType: String? = null,

        var needSwiftCode: Boolean = false,

        var needAccountNumber: Boolean = false

) : BaseEntity()
