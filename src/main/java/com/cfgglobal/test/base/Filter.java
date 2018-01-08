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

import java.text.MessageFormat;
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

    public static final String RELATION_SUFFIX = "_rl";

    public static final String FILTER_PREFIX = "f_";


    @Singular
    List<Condition> conditions = List.empty();

    String relation = RELATION_AND;

    public static List<Filter> createFilters(Map<String, String[]> params) {

        return params.
                filter(it -> it._1().contains("f_") && !it._1().endsWith("_op") && !it._1().endsWith("_rl"))
                .map(it -> {
                    String tempField = it._1();
                    String operator = null;
                    Object value = null;
                    String[] tempValue = it._2();
                    String[] tempOperator = params.get(tempField + OPERATOR_SUFFIX).getOrElse(new String[]{});
                    String[] tempRelation = params.get(tempField + RELATION_SUFFIX).getOrElse(new String[]{});

                    String field = tempField.replace("f_", "");
                    int operatorSize = ArrayUtils.getLength(tempOperator);
                    int valueSize = ArrayUtils.getLength(tempValue);
                    if (operatorSize >= 2) {
                        if (valueSize != operatorSize) {
                            throw new IllegalArgumentException(MessageFormat.format("Operator size and value size of filed [{0}] should be the same, found valueSize [{1}] operatorSize [{2}]", field, valueSize, operatorSize));
                        } else {
                            int relationSize = ArrayUtils.getLength(tempRelation);
                            if (relationSize != 1) {
                                throw new IllegalArgumentException("Relation of [" + field + "]'s length should be 1, found "
                                        + Arrays.toString(tempRelation));
                            }
                        }
                    }
                    Filter filter = new Filter();
                    if (operatorSize >= 2) {
                        filter = filter.relation(RELATION_OR);
                        filter = List.of(tempValue)
                                .zip(List.of(tempOperator))
                                .map(e -> new Condition()
                                        .setFieldName(field)
                                        .setValue(e._1)
                                        .setOperator(e._2)
                                        .setRelation(RELATION_AND))
                                .foldLeft(filter, Filter::addCondition);

                    } else {
                        if (operatorSize == 0) {
                            operator = OPERATOR_LIKE;
                        } else if (operatorSize == 1) {
                            operator = tempOperator[0];
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

                        if (isMultiField(field)) {
                            for (String f : StringUtils.split(field, "-")) {
                                filter.addCondition(f, value, operator, RELATION_OR);
                            }
                        } else {
                            filter.addCondition(field, value, operator);
                        }

                        filter = filter.relation(RELATION_AND);
                    }

                    return filter;
                }).toList();
    }

    private static boolean isMultiField(String field) {
        return StringUtils.contains(field, "-");
    }

    public Filter addCondition(String fieldName, Object value, String operator, String relation) {
        conditions = conditions.append(new Condition(fieldName, value, operator, relation));
        return this;
    }

    public Filter addCondition(String fieldName, Object value, String operator) {
        conditions = conditions.append(new Condition(fieldName, value, operator, Filter.RELATION_AND));
        return this;
    }

    public Filter addCondition(Condition condition) {
        conditions = conditions.append(condition);
        return this;
    }

}
