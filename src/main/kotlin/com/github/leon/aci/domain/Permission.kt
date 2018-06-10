package com.github.leon.aci.domain


import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.persistence.*

@Entity
@Table(name = "aci_permission")
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
        var authUris: String = "",

        @OneToMany(cascade = [(CascadeType.REMOVE)], mappedBy = "permission")
        var rolePermission: MutableList<RolePermission> = mutableListOf()


) : BaseEntity()
