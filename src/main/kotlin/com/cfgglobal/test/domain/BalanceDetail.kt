package com.cfgglobal.ccfx.domain

import com.cfgglobal.ccfx.enums.BalanceDetailTypeEnum
import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert
data class BalanceDetail(
        @NotNull
        var amount: Double = 0.0,

        @Enumerated(value = EnumType.STRING)
        var type: BalanceDetailTypeEnum? = null,

        var reference: String? = null,

        @NotNull
        @Column(unique = true)
        var orderId: String = "",

        @ManyToOne
        @JoinColumn(name = "balance_id")
        var balance: Balance? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User? = null

) : BaseEntity()
