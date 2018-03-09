package com.cfgglobal.test.domain

import com.cfgglobal.test.domain.BaseEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@DynamicUpdate
@DynamicInsert

@Table(uniqueConstraints = [(UniqueConstraint(name = "unique_name", columnNames = arrayOf("name")))])

data class PriceCategory(
        @NotNull
        var name: String = "",
        @OneToMany(mappedBy = "priceCategory", cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
        var pricePolicies: List<PricePolicy> = mutableListOf()
) : BaseEntity(), Serializable {
    override fun toString(): String {
        return "PriceCategory(name='$name')"
    }
}

