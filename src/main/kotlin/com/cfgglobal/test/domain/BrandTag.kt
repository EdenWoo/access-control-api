package com.cfgglobal.ccfx.domain
import com.cfgglobal.generator.metadata.FieldFeature
import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate


import javax.persistence.*
import java.sql.Date
import java.sql.Timestamp
import java.time.ZonedDateTime
import java.util.Objects

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
