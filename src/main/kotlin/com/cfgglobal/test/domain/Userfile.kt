package com.cfgglobal.ccfx.domain
import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate


import javax.persistence.*
import java.sql.Timestamp
import java.util.Objects

@Entity
@DynamicUpdate
@DynamicInsert
class Userfile(
    val path: String = "",
    val originalName: String = "",
    val description: String = "",
    val thumbnail: String = "",
    val isPublic: Boolean = false
): BaseEntity()
