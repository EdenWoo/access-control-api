package com.github.leon.backup.domain

import com.github.leon.aci.domain.Attachment
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.generator.metadata.FieldFeature
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
@DynamicUpdate
@DynamicInsert
data class DbSnapshort(
        var name: String = "",
        @FieldFeature(textarea = true)
        var notes: String = "",
        @OneToOne
        @FieldFeature(hiddenInList = true, hiddenInForm = true)
        var attachment: Attachment


) : BaseEntity()