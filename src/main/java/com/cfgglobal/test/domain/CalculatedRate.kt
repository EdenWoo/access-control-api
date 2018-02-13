package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert
data class CalculatedRate(

        @NotNull
        var currencyPairLeft: String = "",
        @NotNull
        var currencyPairRight: String = "",
        @NotNull
        var operator: String = ""
) : BaseEntity()
