package com.cfgglobal.test.vo

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import java.text.MessageFormat
import java.util.*


class Filter {


    var conditions = listOf<Condition>()

    var relation = RELATION_AND

    fun addCondition(fieldName: String, value: Any?, operator: String?, relation: String): Filter {
        conditions += Condition(fieldName, value, operator!!, relation)
        return this
    }

    fun addCondition(fieldName: String, value: Any?, operator: String?): Filter {
        conditions += (Condition(fieldName, value, operator!!, Filter.RELATION_AND))
        return this
    }

    fun addCondition(condition: Condition): Filter {
        conditions += condition
        return this
    }

    companion object {

        val EMPTY = Filter()

        val OPERATOR_LIKE = "LIKE"

        val OPERATOR_EQ = "="

        val OPERATOR_NOT_EQ = "<>"

        val OPERATOR_GREATER_THAN = ">"

        val OPERATOR_LESS_THEN = "<"

        val OPERATOR_GREATER_EQ = ">="

        val OPERATOR_LESS_EQ = "<="

        val OPERATOR_NULL = "NULL"

        val OPERATOR_NOT_NULL = "NOTNULL"

        val OPERATOR_BETWEEN = "BETWEEN"

        val OPERATOR_IN = "IN"

        val RELATION_AND = "AND"

        val RELATION_OR = "OR"

        val RELATION_NOT = "NOT"

        val OPERATOR_SUFFIX = "_op"

        val RELATION_SUFFIX = "_rl"

        val FILTER_PREFIX = "f_"

        fun createFilters(params: Map<String, Array<String>>): List<Filter> {

            return params.filter { it -> it.key.contains("f_") && !it.key.endsWith("_op") && !it.key.endsWith("_rl") }
                    .map { it ->
                        val tempField = it.key
                        var operator: String? = null
                        var value: Any? = null
                        val tempValue = it.value
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
                        var filter = Filter()
                        if (operatorSize >= 2) {
                            //TODO
                            /*     filter = List.of(*tempValue)
                                         .zip(List.of(*tempOperator))
                                         .map { e ->
                                             (Condition()
                                                     .fieldName = field)
                                                     .setValue(e._1)
                                                     .setOperator(e._2)
                                                     .setRelation(tempRelation[0])
                                         }
                                         .foldLeft({ addCondition() })*/

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

                            if (isMultiField(field)) {
                                for (f in StringUtils.split(field, "-")) {
                                    filter.addCondition(f, value, operator, RELATION_OR)
                                }
                            } else {
                                filter.addCondition(field, value, operator)
                            }
                        }

                        filter.relation = RELATION_AND
                        filter
                    }.toList()
        }

        private fun isMultiField(field: String): Boolean {
            return StringUtils.contains(field, "-")
        }
    }

}
