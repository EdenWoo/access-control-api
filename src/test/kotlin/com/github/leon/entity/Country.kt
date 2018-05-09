package com.github.leon.entity

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.generator.metadata.EntityFeature
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EntityFeature
@Entity
@DynamicUpdate
@DynamicInsert
data class Country(
        @field:NotNull
        @Column(unique = true)
        var name: String,

        @field:NotNull
        var abbr: String,

        @field:NotNull
        var telephonePrefix: String? = null,

        @field:NotNull
        val currency: String

) : BaseEntity()

