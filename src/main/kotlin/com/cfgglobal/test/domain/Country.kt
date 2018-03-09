package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@DynamicUpdate
@DynamicInsert
data class Country(
        var name: String? = null,

        var abbr: String? = null,

        var clearingCodePrefix: String? = null,

        @OneToMany(mappedBy = "country")
        var currencyCountryConfigs: MutableList<CurrencyCountryConfig> = mutableListOf()
) : BaseEntity()
