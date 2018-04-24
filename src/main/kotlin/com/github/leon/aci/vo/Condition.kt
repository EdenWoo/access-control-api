package com.github.leon.aci.vo


data class Condition (
    var fieldName: String? = null,
    var fieldFunction: String? = null,
    var value: Any? = null,
    var operator: String = Filter.OPERATOR_LIKE,
    var relation: String = Filter.RELATION_AND
)
