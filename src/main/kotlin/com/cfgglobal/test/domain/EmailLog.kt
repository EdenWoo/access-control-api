package com.cfgglobal.test.domain

import com.cfgglobal.test.enums.TaskStatus
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.*


@Entity
@DynamicUpdate
@DynamicInsert

data class EmailLog(

        var subject: String? = null,
        @Lob
        @Column(length = 100000)
        @JsonIgnore
        var content: ByteArray? = null,
        var sendTo: String? = null,
        @Enumerated(value = EnumType.STRING)
        var status: TaskStatus? = null,
        var times: Int? = null,
        var msg: String? = null
) : BaseEntity(), Serializable
