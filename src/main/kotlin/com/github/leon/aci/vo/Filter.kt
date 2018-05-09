package com.github.leon.aci.vo

import arrow.core.getOrElse
import arrow.syntax.collections.firstOption
import arrow.core.toOption
import com.github.leon.aci.vo.Filter.Companion.FUNCTION_SUFFIX
import com.github.leon.aci.vo.Filter.Companion.OPERATOR_BETWEEN
import com.github.leon.aci.vo.Filter.Companion.OPERATOR_IN
import com.github.leon.aci.vo.Filter.Companion.OPERATOR_LIKE
import com.github.leon.aci.vo.Filter.Companion.OPERATOR_SUFFIX
import com.github.leon.aci.vo.Filter.Companion.RELATION_AND
import com.github.leon.aci.vo.Filter.Companion.RELATION_OR
import com.github.leon.aci.vo.Filter.Companion.RELATION_SUFFIX
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import java.text.MessageFormat
import java.util.*


data class Filter (
    var conditions:List<Condition> = emptyList(),
    var relation:String = RELATION_AND
){
    companion object {

        val EMPTY = Filter()

        const val OPERATOR_LIKE = "LIKE"

        const val OPERATOR_NOT_LIKE = "NOT_LIKE"

        const val OPERATOR_EQ = "="

        const val OPERATOR_NOT_EQ = "<>"

        const val OPERATOR_GREATER_THAN = ">"

        const val OPERATOR_LESS_THEN = "<"

        const val OPERATOR_GREATER_EQ = ">="

        const val OPERATOR_LESS_EQ = "<="

        const val OPERATOR_NULL = "NULL"

        const val OPERATOR_NOT_NULL = "NOT_NULL"

        const val OPERATOR_BETWEEN = "BETWEEN"

        const val OPERATOR_IN = "IN"

        const val RELATION_AND = "AND"

        const val RELATION_OR = "OR"

        const val RELATION_NOT = "NOT"

        const val OPERATOR_SUFFIX = "_op"

        const val RELATION_SUFFIX = "_rl"

        const val FUNCTION_SUFFIX = "_fun"

        const val FILTER_PREFIX = "f_"

    }

}

fun createFilters(params: Map<String, Array<String>>): List<Filter> {

    return params.filter { it -> it.key.contains("f_") && !it.key.endsWith("_op") && !it.key.endsWith("_rl") &&!it.key.endsWith("_fun")}
            .map { it ->
                val tempField = it.key
                var operator: String? = null
                var value: Any? = null
                val tempValue = it.value
                val fieldFunction = params[tempField + FUNCTION_SUFFIX].toOption().getOrElse { arrayOf() }.firstOption()
                val tempOperator = params[tempField + OPERATOR_SUFFIX].toOption().getOrElse { arrayOf() }
                val tempRelation = params[tempField + RELATION_SUFFIX].toOption().getOrElse { arrayOf() }

                val field = tempField.replace("f_", "")
                val operatorSize = ArrayUtils.getLength(tempOperator)
                val valueSize = ArrayUtils.getLength(tempValue)
                if (operatorSize >= 2) {
                    if (valueSize != operatorSize) {
                        throw IllegalArgumentException(MessageFormat.format("Operator size and value size of filed [{0}] should be the same, found valueSize [{1}] operatorSize [{2}]", field, valueSize, operatorSize))
                    } else {
                        val relationSize = ArrayUtils.getLength(tempRelation)
                        if (relationSize != 1) {
                            throw IllegalArgumentException("Relation of [" + field + "]'s length should be 1, found "
                                    + Arrays.toString(tempRelation))
                        }
                    }
                }
                val filter:Filter
                if (operatorSize >= 2) {
                    val conditions = tempValue.zip(tempOperator)
                            .map { (value, operator) ->
                                Condition().apply {
                                    this.fieldName = field
                                    this.value = value
                                    this.operator = operator
                                    this.relation = tempRelation[0]
                                }
                            }
                    filter = Filter(conditions = conditions)
                } else {
                    if (operatorSize == 0) {
                        operator = OPERATOR_LIKE
                    } else if (operatorSize == 1) {
                        operator = tempOperator[0]
                    }
                    if (valueSize == 1) {
                        value = tempValue[0]
                    } else if (valueSize == 2) {
                        value = tempValue
                        if (operatorSize == 0) {
                            operator = OPERATOR_BETWEEN
                        }
                    } else if (valueSize >= 3) {
                        value = tempValue
                        operator = OPERATOR_IN
                    }

                    filter = when {
                        isMultiField(field) -> {
                            val conditions = field.split("-")
                                    .map {
                                        Condition(fieldName = it,value = value,operator = operator!!,relation = RELATION_OR)
                                    }
                            Filter(conditions = conditions )
                        }
                        else -> Filter(conditions = listOf(
                                Condition(
                                        fieldName = field,
                                        fieldFunction = fieldFunction.orNull(),
                                        value = value,
                                        operator = operator!!,
                                        relation = RELATION_AND)
                        ))
                    }
                }

                filter
            }.toList()
}

private fun isMultiField(field: String): Boolean {
    return StringUtils.contains(field, "-")
}
