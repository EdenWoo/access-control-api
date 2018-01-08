package com.cfgglobal

import com.cfgglobal.test.base.Filter
import io.vavr.API
import io.vavr.collection.List
import io.vavr.collection.Map
import spock.lang.Specification


class FilterTest extends Specification {

    def "CreateFilters"() {
        given:
        Map<String, String[]> map = API.Map()

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 0

    }


    def "CreateFilters throw exception"() {
        given:
        Map<String, String[]> map = API.Map("f_name", ["name"].toArray(new String[0]),
                "f_name_op", ["LIKE", "IN"].toArray(new String[0]))

        when:
        Filter.createFilters(map)

        then:
        thrown(IllegalArgumentException)


    }

    def "CreateFilters default op"() {
        given:
        String[] e = List.of("test").toJavaArray()
        Map<String, String[]> map = API.Map("f_name", e)

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 1
        filters.head().conditions.size() == 1
        filters.head().conditions.head().fieldName == "name"
        filters.head().conditions.head().operator == "LIKE"

    }

    def "CreateFilters like"() {
        given:
        Map<String, String[]> map = API.Map("f_name", ["name"].toArray(new String[0]),
                "f_name_op", ["="].toArray(new String[0]))

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 1
        filters.head().conditions.size() == 1
        filters.head().conditions.head().fieldName == "name"
        filters.head().conditions.head().operator == "="
    }


    def "CreateFilters  2 values between"() {
        given:
        Map<String, String[]> map = API.Map("f_age", ["18", "80"].toArray(new String[0]))

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 1
        filters.head().conditions.size() == 1
        filters.head().conditions.head().fieldName == "age"
        filters.head().conditions.head().operator == "BETWEEN"
        filters.head().conditions.head().value == ["18", "80"]
    }

    def "CreateFilters  3 values in"() {
        given:
        Map<String, String[]> map = API.Map("f_type", ["1", "2", "3"].toArray(new String[0]))

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 1
        filters.head().conditions.size() == 1
        filters.head().conditions.head().fieldName == "type"
        filters.head().conditions.head().operator == "IN"
        filters.head().conditions.head().value == ["1", "2", "3"]
    }


    def "CreateFilters  OR"() {
        given:
        Map<String, String[]> map = API.Map("f_username-email-phone", ["login_information"].toArray(new String[0]))

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 1
        filters.head().conditions.size() == 3

        filters.head().conditions[0].fieldName == "username"
        filters.head().conditions[0].relation == "OR"
        filters.head().conditions[0].value == "login_information"

        filters.head().conditions[1].fieldName == "email"
        filters.head().conditions[1].relation == "OR"
        filters.head().conditions[1].value == "login_information"


        filters.head().conditions[2].fieldName == "phone"
        filters.head().conditions[2].relation == "OR"
        filters.head().conditions[2].value == "login_information"

    }

    def "CreateFilters  Multiple Operators"() {
        //f_status=SETTLE&f_status_op==&f_status=NULL&f_status_op=NULL&f_status_rl=OR
        given :
        Map<String, String[]> map = API.Map(
                "f_status", ["SETTLE", "NULL"].toArray(new String[0]),
                "f_status_op", ["=", "NULL"].toArray(new String[0]),
                "f_status_rl", ["OR"].toArray(new String[0]))

        when:
        List<Filter> filters = Filter.createFilters(map)

        then:
        filters.size() == 1
        filters.head().conditions.size() == 2

        filters.head().conditions[0].fieldName == "status"
        filters.head().conditions[0].value == "SETTLE"

        filters.head().conditions[1].fieldName == "status"
        filters.head().conditions[1].operator == Filter.OPERATOR_NULL
        filters.head().relation("OR")


    }
}