package com.cfgglobal.generator.entity

import com.cfgglobal.generator.metadata.FieldFeature
import com.cfgglobal.test.domain.BaseEntity
import com.github.leon.classpath.ClassSearcher
import com.github.leon.extentions.remainLastIndexOf
import java.lang.reflect.ParameterizedType
import javax.persistence.Column
import javax.persistence.Id
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


data class CodeEntity(
        var fields: List<CodeField> = listOf(),
        var id: Int? = null,

        var name: String,

        var display: String? = null
)


 fun scanForCodeEntities(): List<CodeEntity> {
    return ClassSearcher.of(BaseEntity::class.java).search<BaseEntity?>().map {
        val codeEntity = CodeEntity(
                name = it.simpleName
        )
        val fields = it.declaredFields
                .map {
                    var codeField = CodeField(
                            name = it.name,
                            type = when {
                                List::class.java.isAssignableFrom(it.type) -> FieldType(name = "List", element = (it.genericType as ParameterizedType).actualTypeArguments[0].typeName.remainLastIndexOf("."))
                                BaseEntity::class.java.isAssignableFrom(it.type) -> FieldType(name = "Entity", element = it.type.simpleName)
                                else -> FieldType(name = it.type.simpleName)
                            }
                    )
                    it.declaredAnnotations
                            .forEach {
                                when (it) {
                                    is NotNull-> {
                                        codeField = codeField.copy(required = true)
                                    }
                                    is javax.validation.constraints.Size -> {
                                        codeField = codeField.copy(size = Size(min = it.min, max = it.max))
                                    }
                                    is Max -> {
                                        codeField = codeField.copy(max = it.value)
                                    }
                                    is Min -> {
                                        codeField = codeField.copy(min = it.value)
                                    }
                                    is Column -> {
                                        codeField = codeField.copy(
                                                unique = it.unique,
                                                required = !it.nullable
                                        )
                                    }
                                    is FieldFeature -> {
                                        codeField = codeField.copy(
                                                searchable = it.searchable,
                                                sortable = it.sortable,
                                                limit = it.limit)
                                    }
                                    is Id -> codeField = codeField.copy(primaryKey = true)

                                }
                            }
                    codeField
                }
        codeEntity.fields = fields
        codeEntity
    }
}
