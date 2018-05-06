package com.github.leon.aci.domain

import org.hibernate.annotations.Type
import javax.persistence.Entity


@Entity
data class FindPwdSendLog(
        var email: String? = null,
        var times: Int? = null,
        var inputStr: String? = null,
        var expireDate: Long? = null,
        var doDate: Long? = null,
        @Type(type = "yes_no")
        var used: Boolean? = null

) : BaseEntity()
