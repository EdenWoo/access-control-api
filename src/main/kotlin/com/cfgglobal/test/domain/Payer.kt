package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.Attachment
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
data class Payer(

        @NotNull
        var name: String = "",


        @NotNull
        var address: String = "",


        @NotNull
        var city: String = "",


        @NotNull
        var country: String = "",


        @OneToMany(cascade = [(CascadeType.PERSIST), (CascadeType.REFRESH), (CascadeType.MERGE)], orphanRemoval = true)
        var attachments: MutableList<Attachment> = mutableListOf(),


        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User? = null,


        @OneToMany(mappedBy = "payer")
        var payees: MutableList<Payee> = mutableListOf(),


        @OneToMany(mappedBy = "payer")
        var transactions: MutableList<Transaction> = mutableListOf(),

        @Type(type = "yes_no")
        @NotNull
        var verify: Boolean = false


) : BaseEntity()
