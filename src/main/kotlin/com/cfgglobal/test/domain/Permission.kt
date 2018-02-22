package com.cfgglobal.test.domain


import lombok.NonNull
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Entity

@Entity
@DynamicUpdate
@DynamicInsert
data class Permission(

        @NonNull
        var entity: String = "",

        @NonNull
        var authKey: String = "",

        @NonNull
        var httpMethod: String = "",

        @NonNull
        var authUris: String = ""

) : BaseEntity()
