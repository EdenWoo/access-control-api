package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert

data class Deposit(

        @ManyToOne
        @JoinColumn(name = "user_id")
        @NotNull
        var user: User? = null,

        @NotNull
        var orderId: String = "",

        @NotNull
        var currency: String = "",

        @NotNull
        var amount: Double = 0.0,

        @Type(type = "yes_no")
        @NotNull
        var clientFund: Boolean = false,

        var notes: String = "",

        @Type(type = "yes_no")
        @NotNull
        var received: Boolean = false


) : BaseEntity()
