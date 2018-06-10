package com.github.leon.aci.domain


import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "aci_rule")
data class Rule(

        @field:NotNull
        val name: String = "",

        val params: String? = null,

        @field:NotNull
        val type: String? = null,

        @Type(type = "yes_no")
        @field:NotNull
        val enable: Boolean = true

) : BaseEntity()
