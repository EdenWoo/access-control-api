package com.github.leon.generator.entity

import arrow.core.getOrElse
import arrow.syntax.collections.tail
import arrow.core.toOption
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.classpath.ClassSearcher
import com.github.leon.extentions.remainLastIndexOf
import com.github.leon.generator.findClasses
import com.github.leon.generator.metadata.EntityFeature
import com.github.leon.generator.metadata.ExcelFeature
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
        var listFields: List<CodeField> = listOf(),
        var requiredFields: List<CodeField> = listOf(),


        var fields: List<CodeField> = listOf(),
        var id: Int? = null,
        var code: Int = 0,
        var name: String,

        var display: String? = null,
        var security: Boolean = true,
        var tree: Boolean = false

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

fun scanForCodeEntities(path:String): List<CodeEntity> {
    val entities  = findClasses(BaseEntity::class.java, path)
    return entities
            .filter {
                it.getDeclaredAnnotation(EntityFeature::class.java).toOption().map { it.generated }.getOrElse { true }
            }.map(entityClass2CodeEntity())
}

fun entityClass2CodeEntity(): (Class<*>) -> CodeEntity {
    return {
        var codeEntity = CodeEntity(
                name = it.simpleName
        )
        var ignoredFields = listOf("serialVersionUID", "Companion")

        it.declaredAnnotations.forEach {
            when (it) {
                is EntityFeature -> {
                    if (!it.createdAtInList) {
                        ignoredFields += "createdAt"
                    }
                    if (!it.creatorInList) {
                        ignoredFields += "creator"
                    }
                    if (!it.updatedAtInList) {
                        ignoredFields += "updatedAt"
                    }
                    if (!it.modifierInList) {
                        ignoredFields += "modifier"
                    }
                    if (!it.versionInList) {
                        ignoredFields += "version"
                    }
                    codeEntity = codeEntity.copy(
                            code = it.code,
                            security = it.security,
                            tree = it.tree
                    )
                }
            }
        }
        val fields = (it.superclass.declaredFields + it.declaredFields)
                .filter { ignoredFields.all { ignoreField -> ignoreField != it.name } }
                .map { field ->
                    var codeField = CodeField(
                            name = field.name,
                            type = when {
                                List::class.java.isAssignableFrom(field.type) ->
                                    FieldType(name = "List",
                                            element = (field.genericType as ParameterizedType).actualTypeArguments[0]
                                                    .typeName.remainLastIndexOf("."))
                                BaseEntity::class.java.isAssignableFrom(field.type) ->
                                    FieldType(name = "Entity", element = field.type.simpleName)
                                else -> FieldType(name = field.type.simpleName)
                            }
                    )
                    field.declaredAnnotations
                            .forEach {
                                when (it) {
                                    is NotNull -> {
                                        codeField = codeField.copy(required = true)
                                    }
                                    is Size -> {
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
                                    is Id -> codeField = codeField.copy(primaryKey = true)

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
                                                display = it.display.split(","),
                                                weight = it.weight,
                                                range = it.range,
                                                label = it.label
                                        )
                                    }
                                    is ExcelFeature -> {
                                        codeField = codeField.copy(
                                                column = if (it.column.isNotBlank()) {
                                                    it.column
                                                } else {
                                                    field.name
                                                },
                                                header = if (it.header.isNotBlank()) {
                                                    it.header
                                                } else {
                                                    field.name
                                                },
                                                exportable = it.exportable,
                                                importable = it.importable
                                        )
                                    }

                                }
                            }
                    codeField
                }
        codeEntity.fields = fields
        val (formHiddenFields, otherFields) = fields.partition { it.hiddenInForm || it.primaryKey }
        codeEntity.formHiddenFields = formHiddenFields
        codeEntity.groupedFields = groupFields(otherFields).takeWhile { it.isNotEmpty() }.toList()
        val (_, listFields) = fields.partition { it.hiddenInList || it.primaryKey }
        codeEntity.listFields = listFields
        codeEntity.requiredFields = fields.filter { it.required }
        codeEntity
    }
}


fun groupFields(codeFields: List<CodeField>): Sequence<List<CodeField>> = buildSequence {
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
