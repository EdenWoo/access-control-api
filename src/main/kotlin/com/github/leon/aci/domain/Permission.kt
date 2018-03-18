package com.github.leon.aci.domain


import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert
data class Permission(

        @NotNull
        var entity: String = "",

        @NotNull
        var authKey: String = "",

        @NotNull
        var httpMethod: String = "",

        @NotNull
        var authUris: String = ""

) : BaseEntity()
