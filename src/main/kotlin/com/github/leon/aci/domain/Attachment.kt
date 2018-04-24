package com.github.leon.aci.domain

import com.github.leon.aci.enums.AttachmentType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@DynamicUpdate
@DynamicInsert
data class Attachment(

        @NotNull
        var name: String = "",

        var contentType: String? = null,

        @NotNull
        var size: Long = -1,

        @NotNull
        var originalFilename: String = "",

        var notes: String? = null,

        @NotNull
        @Enumerated(value = EnumType.STRING)
        var type: AttachmentType = AttachmentType.CUSTOMER_BANK_ACCOUNT_DOC,

        @NotNull
        var fullPath: String = ""
) : BaseEntity()
