package com.cfgglobal.ccfx.domain

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.enums.TaskStatus
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated


@Entity
@DynamicUpdate
@DynamicInsert
data class MessageLog(
        var sendTo: String = "",
        var message: String = "",
        @Enumerated(value = EnumType.STRING)
        var status: TaskStatus? = null,
        var resp: String = ""
) : BaseEntity(), Serializable
