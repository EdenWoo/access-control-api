package com.cfgglobal.test.domain

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class SysConfig(
        val confKey: String = "",
        val confVal: String = ""
) : BaseEntity()
