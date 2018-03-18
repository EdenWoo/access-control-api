package com.github.leon.aci.web.api.vo

import com.github.leon.aci.enums.TaskStatus
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Lob

data class EmailLogVo(

        var subject: String? = null,
        @Lob
        @Column(length = 100000)
        var content: ByteArray? = null,
        var html: String? = null,
        var sendTo: String? = null,
        @Enumerated(value = EnumType.STRING)
        var status: TaskStatus? = null,
        var msg: String? = null
)
