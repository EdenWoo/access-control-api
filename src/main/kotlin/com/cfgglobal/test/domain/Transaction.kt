package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import java.time.ZonedDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert

@Table(uniqueConstraints = [(UniqueConstraint(name = "unique_order_id", columnNames = arrayOf("orderId")))])
data class Transaction(

        @NotNull
        @Column(unique = true)
        var orderId: String = "",

        @NotNull
        var buyCurrency: String = "",

        @NotNull
        var buyAmount: Double = 0.0,

        @NotNull
        var sellCurrency: String = "",

        @NotNull
        var sellAmount: Double = 0.0,

        var payAmount: Double? = null,

        var baseRate: Double? = null,

        var salesRate: Double? = null,

        @NotNull
        var rate: Double = 0.0,

        var expectedRate: Double? = null,

        var expireAt: ZonedDateTime? = null,

        @Enumerated(value = EnumType.STRING)
        var status: String? = null,

        @NotNull
        @Enumerated(value = EnumType.STRING)
        var type: String? = null,

        var paymentReason: String? = null,

        @ManyToOne
        @JoinColumn(name = "parent_id")
        var parent: Transaction? = null,

        @OneToMany(mappedBy = "parent")
        var children: MutableList<Transaction> = mutableListOf(),

        var overnightRate: Int? = null,

        var serviceFee: Double? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User? = null,

        @ManyToOne
        @JoinColumn(name = "payer_id")
        var payer: Payer? = null,

        @ManyToOne
        @JoinColumn(name = "payee_id")
        var payee: Payee? = null,

        @ManyToOne
        @JoinColumn(name = "payee_bank_account_id")
        var payeeBankAccount: PayeeBankAccount? = null,

        @Type(type = "yes_no")
        @NotNull
        var leaf: Boolean? = null,

        @Type(type = "yes_no")
        var checked: Boolean = false,

        @ManyToOne
        @JoinColumn(name = "checker_id")
        var checkedBy: User? = null,

        @ManyToOne
        @JoinColumn(name = "processor_id")
        var processedBy: User? = null,

        @ManyToOne
        @JoinColumn(name = "canceler_id")
        var canceledBy: User? = null,

        @ManyToOne
        @JoinColumn(name = "settler_id")
        var settledBy: User? = null,

        @ManyToOne
        @JoinColumn(name = "rejector_id")
        var rejectedBy: User? = null,

        @Column
        var checkedAt: ZonedDateTime? = null,

        @Column
        var processedAt: ZonedDateTime? = null,

        @Column
        var canceledAt: ZonedDateTime? = null,

        @Column
        var settledAt: ZonedDateTime? = null,

        @Column
        var bookedAt: ZonedDateTime? = null,

        @Column
        var rejectedAt: ZonedDateTime? = null,

        @Type(type = "yes_no")
        var sameday: Boolean = false,

        @Transient
        var reversed: Boolean = false,

        @Transient
        var sendEmail: Boolean = false,

        @Transient
        var calculateFromBuyToSell: String? = null

) : BaseEntity()
