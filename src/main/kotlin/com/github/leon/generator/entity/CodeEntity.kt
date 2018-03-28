package com.github.leon.generator.entity

import arrow.core.Some
import arrow.syntax.collections.tail
import arrow.syntax.option.toOption
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.classpath.ClassSearcher
import com.github.leon.extentions.remainLastIndexOf
import com.github.leon.generator.metadata.EntityFeature
import com.github.leon.generator.metadata.FieldFeature
import org.hibernate.validator.constraints.Range
import org.joor.Reflect
import java.lang.reflect.ParameterizedType
import javax.persistence.Column
import javax.persistence.Id
import javax.validation.constraints.*
import kotlin.coroutines.experimental.buildSequence


data class CodeEntity(
        var groupedFields: List<List<CodeField>> = listOf(),
        var formHiddenFields: List<CodeField> = listOf(),
        var fields: List<CodeField> = listOf(),
        var id: Int? = null,

        var name: String,

        var display: String? = null
)

fun scanForCodeEnum(): List<CodeEnum> {
    return ClassSearcher.of(Enum::class.java).search<Enum<*>>().map {
        CodeEnum(
                name = it.name.remainLastIndexOf("."),
                value = (Reflect.on(it).call("values").get() as Array<*>)
                        .map { it.toString().remainLastIndexOf(".") }
                        .toList())
    }
}

fun scanForCodeEntities(): List<CodeEntity> {

    return ClassSearcher.of(BaseEntity::class.java).search<BaseEntity?>().map {
        val codeEntity = CodeEntity(
                name = it.simpleName
        )
        var ignoredFields = listOf("serialVersionUID", "Companion")
        val entityFeature = it.getDeclaredAnnotation(EntityFeature::class.java).toOption()
        when (entityFeature) {
            is Some -> {
                val en = entityFeature.t
                if (!en.createdAtInList) {
                    ignoredFields += "createdAt"
                }
                if (!en.creatorInList) {
                    ignoredFields += "creator"
                }
                if (!en.updatedAtInList) {
                    ignoredFields += "updatedAt"
                }
                if (!en.modifierInList) {
                    ignoredFields += "modifier"
                }
                if (!en.version) {
                    ignoredFields += "version"
                }
            }
        }
        val fields = (it.declaredFields + it.superclass.declaredFields)
                .filter({ ignoredFields.all { ignoreField -> ignoreField != it.name } })
                .map {
                    var codeField = CodeField(
                            name = it.name,
                            type = when {
                                List::class.java.isAssignableFrom(it.type) ->
                                    FieldType(name = "List",
                                            element = (it.genericType as ParameterizedType).actualTypeArguments[0].typeName.remainLastIndexOf("."))
                                BaseEntity::class.java.isAssignableFrom(it.type) ->
                                    FieldType(name = "Entity", element = it.type.simpleName)
                                else -> FieldType(name = it.type.simpleName)
                            }
                    )
                    it.declaredAnnotations
                            .forEach {
                                when (it) {
                                    is NotNull -> {
                                        codeField = codeField.copy(required = true)
                                    }
                                    is javax.validation.constraints.Size -> {
                                        codeField = codeField.copy(sizeMin = it.min, sizeMax = it.max)
                                    }
                                    is Max -> {
                                        codeField = codeField.copy(rangeMax = it.value)
                                    }
                                    is Min -> {
                                        codeField = codeField.copy(rangeMin = it.value)
                                    }
                                    is Range -> {
                                        codeField = codeField.copy(rangeMin = it.min, rangeMax = it.max)
                                    }
                                    is Pattern -> {
                                        codeField = codeField.copy(pattern = it.regexp)
                                    }
                                    is Future -> {
                                        codeField = codeField.copy(future = true)
                                    }

                                    is Past -> {
                                        codeField = codeField.copy(past = true)
                                    }
                                    is Column -> {
                                        codeField = codeField.copy(
                                                unique = it.unique
                                        )
                                    }
                                    is FieldFeature -> {
                                        codeField = codeField.copy(
                                                searchable = it.searchable,
                                                sortable = it.sortable,
                                                switch = it.switch,
                                                attachment = it.attachment,
                                                selectOne = it.selectOne,
                                                selectMany = it.selectMany,
                                                addDynamicMany = it.addDynamicMany,
                                                hiddenInForm = it.hiddenInForm,
                                                hiddenInList = it.hiddenInList,
                                                limit = it.limit,
                                                textarea = it.textarea,
                                                richText = it.richText,
                                                display = it.display,
                                                weight = it.weight,
                                                range = it.range
                                        )
                                    }
                                    is Id -> codeField = codeField.copy(primaryKey = true)

                                }
                            }
                    codeField
                }
        codeEntity.fields = fields
        val (formHiddenFields, otherFields) = fields.partition { it.hiddenInForm || it.primaryKey }
        codeEntity.formHiddenFields = formHiddenFields//.sortedBy { it.order }
        codeEntity.groupedFields = subOrders(otherFields).takeWhile { it.isNotEmpty() }.toList()
        codeEntity
    }
}


fun subOrders(codeFields: List<CodeField>): Sequence<List<CodeField>> = buildSequence {
    var terms = codeFields
    while (true) {
        when {
            terms.isEmpty() -> {
                yield(emptyList())
                terms = emptyList()
            }
            terms.tail().isEmpty() -> {
                yield(listOf(terms.first()))
                terms = terms.tail()
            }
            terms.first().weight + terms.tail().first().weight > 12 -> {
                yield(listOf(terms.first()))
                terms = terms.tail()
            }
            terms.first().weight + terms.tail().first().weight == 12 -> {
                yield(listOf(terms.first(), terms.tail().first()))
                terms = terms.tail().tail()
            }
        }
    }
}
