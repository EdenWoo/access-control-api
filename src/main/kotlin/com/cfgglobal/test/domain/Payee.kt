package com.cfgglobal.test.domain

import com.cfgglobal.generator.metadata.FieldFeature
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert
data class Payee(

        @Column(nullable = false)
        @NotNull
        var name: String = "",

        @Column(nullable = false)
        @NotNull
        var country: String = "",


        @NotNull
        @Length(max = 35)
        var addressLine1: String = "",


        @NotNull
        @Length(max = 35)
        var addressLine2: String = "",


        @NotNull
        var city: String = "",


        var postCode: String? = null,


        @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
        var attachments: MutableList<Attachment> = mutableListOf(),

        @FieldFeature(switch = true, display = true, sortable = true)
        @Type(type = "yes_no")
        @NotNull
        var enable: Boolean? = false,

        @Column(nullable = false)
        @ManyToOne
        @JoinColumn(name = "payer_id")
        var payer: Payer? = null,


        @OneToMany(mappedBy = "payee", cascade = [(CascadeType.ALL)], orphanRemoval = true)
        var payeeBankAccounts: MutableList<PayeeBankAccount> = mutableListOf(),

        @Column(nullable = false)
        @Transient
        var payAmount: BigDecimal? = null,

        @OneToMany(mappedBy = "payee", cascade = [(CascadeType.PERSIST), (CascadeType.MERGE)], fetch = FetchType.LAZY)
        var transactions: MutableList<Transaction> = mutableListOf(),

        @Column(nullable = false)
        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User? = null,

        @Type(type = "yes_no")
        var verify: Boolean? = false


) : BaseEntity()
