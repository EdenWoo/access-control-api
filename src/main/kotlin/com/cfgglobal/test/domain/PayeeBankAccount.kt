package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert
class PayeeBankAccount : BaseEntity() {

    @NotNull
    var accountName: String = ""

    var accountNumber: String? = null

    @NotNull
    var accountCountry: String = ""


    @NotNull
    var currency: String = ""

    @NotNull
    var bankName: String = ""


    @NotNull
    var branchName: String? = null


    var clearingCode: String? = null


    var clearingCodeType: String? = null


    var swiftCode: String? = null


    var branchCity: String? = null


    var branchAddress: String? = null


    @ManyToOne
    @JoinColumn(name = "payee_id")
    var payee: Payee? = null


    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null


}
