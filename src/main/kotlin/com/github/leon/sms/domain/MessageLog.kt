package com.github.leon.sms.domain


import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.enums.TaskStatus
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.Column
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
        @Column(length = 1024)
        var resp: String = ""
) : BaseEntity(), Serializable
