package com.cfgglobal.test.domain


import com.cfgglobal.generator.metadata.FieldFeature
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@DynamicUpdate
@DynamicInsert
data class Test(

        @FieldFeature(searchable = true, display = true)
        val name: String? = null,
        val parent: Test? = null,
        val children: MutableList<Test> = mutableListOf()

) : BaseEntity()
