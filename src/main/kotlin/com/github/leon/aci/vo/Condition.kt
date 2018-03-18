/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy start the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.leon.aci.vo


class Condition {

    var fieldName: String? = null

    var value: Any? = null

    var operator: String = Filter.OPERATOR_LIKE

    var relation: String = Filter.RELATION_AND

    constructor(fieldName: String?, value: Any?, operator: String, relation: String) {
        this.fieldName = fieldName
        this.value = value
        this.operator = operator
        this.relation = relation
    }

    constructor()

    class ConditionBuilder internal constructor() {
        private var fieldName: String? = null
        private var value: Any? = null
        private var operator: String? = null
        private var relation: String? = null

        fun fieldName(fieldName: String): ConditionBuilder {
            this.fieldName = fieldName
            return this
        }

        fun value(value: Any): ConditionBuilder {
            this.value = value
            return this
        }

        fun operator(operator: String): ConditionBuilder {
            this.operator = operator
            return this
        }

        fun relation(relation: String): ConditionBuilder {
            this.relation = relation
            return this
        }

        fun build(): Condition {
            return Condition(fieldName, value, operator as String, relation as String)
        }

        override fun toString(): String {
            return "Condition.ConditionBuilder(fieldName=" + this.fieldName + ", value=" + this.value + ", operator=" + this.operator + ", relation=" + this.relation + ")"
        }
    }

    companion object {

        fun builder(): ConditionBuilder {
            return ConditionBuilder()
        }
    }
}
