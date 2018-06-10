package com.github.leon.aci.domain


import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Table(name = "aci_branch")
@Entity
@DynamicUpdate
@DynamicInsert
data class Branch(

        @NotNull
        val name: String? = null,

        @ManyToOne(cascade = [(CascadeType.REFRESH), (CascadeType.REMOVE)], optional = true)
        @JoinColumn(name = "parent_id")
        val parent: Branch? = null,
        @OneToMany(cascade = [(CascadeType.REFRESH), (CascadeType.REMOVE)], fetch = FetchType.LAZY, mappedBy = "parent")
        val children: MutableList<Branch> = mutableListOf()

) : BaseEntity()
