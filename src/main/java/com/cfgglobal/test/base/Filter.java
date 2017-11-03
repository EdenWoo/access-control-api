package com.cfgglobal.test.base;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class Filter {

    public static final Filter EMPTY = new Filter();

    public static final String OPERATOR_LIKE = "LIKE";

    public static final String OPERATOR_EQ = "=";

    public static final String OPERATOR_NOT_EQ = "<>";

    public static final String OPERATOR_GREATER_THAN = ">";

    public static final String OPERATOR_LESS_THEN = "<";

    public static final String OPERATOR_GREATER_EQ = ">=";

    public static final String OPERATOR_LESS_EQ = "<=";

    public static final String OPERATOR_NULL = "NULL";

    public static final String OPERATOR_NOT_NULL = "NOTNULL";

    public static final String OPERATOR_BETWEEN = "BETWEEN";

    public static final String OPERATOR_IN = "IN";

    public static final String RELATION_AND = "AND";

    public static final String RELATION_OR = "OR";

    public static final String RELATION_NOT = "NOT";

    public static final String OPERATOR_SUFFIX = "_op";

    public static final String FILTER_PREFIX = "f_";


    @Singular
    List<Condition> conditions = List.empty();

    String relation = RELATION_AND;

    public static List<Filter> createFilters(Map<String, String[]> params) {

        return params.
                filter(it -> it._1().contains("f_") && !it._1().endsWith("_op"))
                .map(it -> {
                    String field = it._1();
                    String operator;
                    Object value = null;
                    String[] tempValue = it._2();
                    String[] tempOperator = params.get(field + OPERATOR_SUFFIX).getOrElse(new String[]{});
                    field = field.replace("f_", "");
                    int operatorSize = ArrayUtils.getLength(tempOperator);
                    int valueSize = ArrayUtils.getLength(tempValue);
                    if (operatorSize >= 2) {
                        throw new IllegalArgumentException("Operator start[" + field + "]'s length should < 2, found "
                                + Arrays.toString(tempOperator));
                    }

                    if (operatorSize == 1) {
                        operator = tempOperator[0];
                    } else {
                        operator = OPERATOR_LIKE;
                    }

                    if (valueSize == 1) {
                        value = tempValue[0];
                    } else if (valueSize == 2) {
                        value = tempValue;
                        if (operatorSize == 0) {
                            operator = OPERATOR_BETWEEN;
                        }
                    } else if (valueSize >= 3) {
                        value = tempValue;
                        operator = OPERATOR_IN;

                    }
                    Filter filter = new Filter();

                    if (StringUtils.contains(field, "-")) {
                        for (String f : StringUtils.split(field, "-")) {
                            filter.addCondition(f, value, operator, RELATION_OR);
                        }
                    } else {
                        filter.addCondition(field, value, operator);
                    }
                    return filter.relation(RELATION_AND);
                }).toList();
    }

    public Filter addCondition(String fieldName, Object value, String operator, String relation) {
        conditions = conditions.append(new Condition(fieldName, value, operator, relation));
        return this;
    }

    public Filter addCondition(String fieldName, Object value, String operator) {
        conditions = conditions.append(new Condition(fieldName, value, operator, Filter.RELATION_AND));
        return this;
    }

}
