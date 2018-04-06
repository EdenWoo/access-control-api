package com.github.leon.sysconfig

import com.github.leon.aci.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class SysConfig(
        var confKey: String = "",
        var confVal: String = ""
) : BaseEntity()
