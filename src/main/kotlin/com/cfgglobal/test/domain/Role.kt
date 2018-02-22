package com.cfgglobal.test.domain

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert

data class Role(
        @NotNull
        var name: String = "",

        @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)])
        var users: MutableList<User> = mutableListOf(),


        @OneToMany(cascade = [(CascadeType.PERSIST), (CascadeType.REFRESH), (CascadeType.MERGE)], fetch = FetchType.LAZY, orphanRemoval = true)
        @JoinColumn(name = "role_id")
        val rolePermissions: MutableList<RolePermission> = mutableListOf()

) : BaseEntity()
