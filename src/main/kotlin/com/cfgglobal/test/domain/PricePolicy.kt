package com.cfgglobal.test.domain


import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert
@Table(uniqueConstraints = [(UniqueConstraint(name = "unique_buyCurrency_sellCurrency_priceCategory", columnNames = arrayOf("buyCurrency", "sellCurrency", "price_category_id")))])

data class PricePolicy(
        @NotNull
        var buyCurrency: String = "",
        @NotNull
        var sellCurrency: String = "",
        @NotNull
        var sysBuyMargin: Int = 0,
        @NotNull
        var sysSellMargin: Int = 0,

        @NotNull
        var bankBuyMargin: Int = 0,
        @NotNull
        var bankSellMargin: Int = 0,

        @ManyToOne
        @JoinColumn(name = "price_category_id")
        var priceCategory: PriceCategory? = null,

        @Transient
        var baseBuyPrice: String? = null,

        @Transient
        var baseSellPrice: String? = null,

        @Transient
        var sysBuyPrice: String? = null,

        @Transient
        var sysSellPrice: String? = null,

        @Transient
        var bankBuyPrice: String? = null,

        @Transient
        var bankSellPrice: String? = null

) : BaseEntity() {
    override fun toString(): String {
        return "PricePolicy(buyCurrency='$buyCurrency', sellCurrency='$sellCurrency', sysBuyMargin=$sysBuyMargin, sysSellMargin=$sysSellMargin, bankBuyMargin=$bankBuyMargin, bankSellMargin=$bankSellMargin, priceCategory=$priceCategory, baseBuyPrice=$baseBuyPrice, baseSellPrice=$baseSellPrice, sysBuyPrice=$sysBuyPrice, sysSellPrice=$sysSellPrice, bankBuyPrice=$bankBuyPrice, bankSellPrice=$bankSellPrice)"
    }
}