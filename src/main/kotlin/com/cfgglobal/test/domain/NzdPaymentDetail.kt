package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity

@DynamicUpdate
@DynamicInsert
@Entity
class NzdPaymentDetail : BaseEntity() {
    var beneficiaryName: String? = null

    var accountNumber: String? = null
    var bank: String? = null

    var branch: String? = null

    var amount: Double? = null

    var reference: String? = null

}
