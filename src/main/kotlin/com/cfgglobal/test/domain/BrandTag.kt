package com.cfgglobal.ccfx.domain


import com.github.leon.aci.domain.BaseEntity
import com.github.leon.generator.metadata.FieldFeature
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToMany

@Entity
@DynamicUpdate
@DynamicInsert
class BrandTag(
        val name: String = "",
        val registerDate: ZonedDateTime? = null,
        val registerCode: String = "",
        val popularity: Int = 0,
        val website: String = "",
        val isVerified: Boolean = false,
//        val registerCompany: Company? = null,
//        val registerNation: Nation? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        val userfiles: MutableList<Userfile> = mutableListOf(),

        @FieldFeature(boolean = true)
        val isOriginal: Boolean = false

) : BaseEntity()
