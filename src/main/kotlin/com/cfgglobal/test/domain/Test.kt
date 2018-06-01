package com.cfgglobal.test.domain


import com.github.leon.aci.domain.BaseEntity
import com.github.leon.generator.metadata.FieldFeature
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@DynamicUpdate
@DynamicInsert
data class Test(

        @FieldFeature(searchable = true)
        val name: String? = null,
        val parent: Test? = null,
        val children: MutableList<Test> = mutableListOf()

) : BaseEntity()
