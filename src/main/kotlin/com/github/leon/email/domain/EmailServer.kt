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
        var host: String = "owa.onenet.co.nz",
        var timeout: Int = 25000,
        var port: Int = 587,
        var alias: String = "info.ccfx@collinsonfx.com",
        var fromAddress: String = "info.ccfx@collinsonfx.com",
        var username: String = "info.ccfx@collinsonfx.com",
        var password: String = "Kedo9140"
) : BaseEntity(), Serializable
