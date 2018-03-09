package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.Attachment
import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@DynamicUpdate
@DynamicInsert
@Entity

data class NzdPayment(

        var notes: String? = null,

        @OneToMany(cascade = [(CascadeType.ALL)])
        var attachments: MutableList<Attachment> = mutableListOf(),

        @OneToMany(cascade = [(CascadeType.ALL)])
        @JoinColumn(name = "nzd_payment_id")
        var nzdPaymentDetails: MutableList<NzdPaymentDetail> = mutableListOf()

) : BaseEntity()
