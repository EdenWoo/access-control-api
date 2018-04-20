package com.github.leon.email.domain

import com.github.leon.aci.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated


@Entity
@DynamicUpdate
@DynamicInsert

data class EmailServer(

        val host: String = "owa.onenet.co.nz",
        val timeout: Int = 25000,
        val port: Int = 587,
        val alias: String = "info.ccfx@collinsonfx.com",
        val from: String = "info.ccfx@collinsonfx.com",
        val username: String = "info.ccfx@collinsonfx.com",
        val password: String = "Kedo9140",
        @Enumerated(value = EnumType.STRING)
        val active:Boolean

) : BaseEntity(), Serializable
