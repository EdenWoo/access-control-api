package com.github.leon.aci.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
data class FindPwdSendLog(
        @Id
        @GeneratedValue
        var ip: String? = null,
        var email: String? = null,
        var times: Int? = null,
        var inputStr: String? = null,
        var expireDate: Long? = null,
        var doDate: Long? = null,
        var isUsed: Int? = null

) : BaseEntity()
