package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert

class CollinsonUser : User() {

    @NotNull
    var address: String = ""

    @NotNull
    var city: String = ""

    @NotNull
    var country: String = ""


    var maxPerTrx: Double? = null


    var minPerTrx: Double? = null


    var phone: String? = null


    @OneToMany(mappedBy = "user")
    var transactions: MutableList<Transaction> = mutableListOf()


    @OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
    var payers: MutableList<Payer> = mutableListOf()


    @OneToOne
    var priceCategory: PriceCategory? = null

    companion object {
        var serialVersionUID = 1L
    }


}
