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

        @NotNull
        val name: String = "",

        @NotNull
        val params: String? = null,

        @NotNull
        val type: String? = null,

        @Type(type = "yes_no")
        @NotNull
        val enable: Boolean = true

) : BaseEntity()
