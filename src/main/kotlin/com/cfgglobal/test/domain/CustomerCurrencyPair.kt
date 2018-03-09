package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Transient


@Entity
@DynamicUpdate
@DynamicInsert

class CustomerCurrencyPair : BaseEntity(), Serializable {

    var buyCurrency: String? = null
    var sellCurrency: String? = null
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null

    @Transient
    var rate: Double? = null
}