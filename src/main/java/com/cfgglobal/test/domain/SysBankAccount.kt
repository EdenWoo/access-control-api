package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert
data class SysBankAccount(
        @NotNull
        var currency: String? = null,

        var accountName: String? = null,

        var accountNumber: String? = null,

        var bankName: String? = null,

        var bankAddress: String? = null,

        var bankCity: String? = null,

        var bankCountry: String? = null,

        var bsbNumber: String? = null,

        var bicCode: String? = null,

        var correspondentBank: String? = null

) : BaseEntity()
