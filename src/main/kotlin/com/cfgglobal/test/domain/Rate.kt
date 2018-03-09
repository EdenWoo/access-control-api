package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Transient
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert

data class Rate(

        @NotNull
        var buyCurrency: String = "",

        @NotNull
        var sellCurrency: String = "",


        @NotNull
        @Column(precision = 10, scale = 5)
        var buyPrice: Double = 0.0,


        @NotNull
        @Column(precision = 10, scale = 5)
        var sellPrice: Double = 0.0,


        @Transient
        var salesBuyPrice: Double = 0.0,


        @Transient
        var salesSellPrice: Double = 0.0,

        @Transient
        var clientBuyPrice: Double = 0.0,

        @Transient
        var clientSellPrice: Double = 0.0
) : BaseEntity()
