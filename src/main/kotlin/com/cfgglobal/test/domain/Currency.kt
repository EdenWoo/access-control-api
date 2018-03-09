package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert

class Currency(

        var name: String? = null,

        @NotNull
        var maxPerTrx: Double = 0.0,

        @NotNull
        var minPerTrx: Double = 0.0,

        @NotNull
        var smsAlertAmt: Double = 0.0,

        @OneToMany(mappedBy = "currency")
        var currencyCountryConfigs: MutableList<CurrencyCountryConfig> = mutableListOf()

) : BaseEntity()
