package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity

@DynamicUpdate
@DynamicInsert
@Table(uniqueConstraints = [(UniqueConstraint(
        name = "unique_user_currency",
        columnNames = arrayOf("user_id", "currency", "clientFund")
))])
data class Balance(

        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User? = null,

        @NotNull
        var currency: String = "",

        @Type(type = "yes_no")
        var clientFund: Boolean = false,

        @OneToMany
        @JoinColumn(name = "balance_id")
        var balanceDetails: MutableList<BalanceDetail> = mutableListOf(),

        var opening: Double = 0.0,

        var endOfTheDay: Double = 0.0

) : BaseEntity() {
        override fun toString(): String {
                return "Balance(user=$user, currency='$currency', clientFund=$clientFund, opening=$opening, endOfTheDay=$endOfTheDay)"
        }

}
