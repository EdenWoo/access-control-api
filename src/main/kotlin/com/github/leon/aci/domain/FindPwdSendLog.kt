package com.github.leon.aci.domain

import org.hibernate.annotations.Type
import java.time.ZonedDateTime
import javax.persistence.Entity


@Entity
data class FindPwdSendLog(
        var username: String? = null,
        var email: String? = null,
        var expireDate: ZonedDateTime? = null,
        @Type(type = "yes_no")
        var used: Boolean? = null,
        var encryptId: String? = null
) : BaseEntity()
