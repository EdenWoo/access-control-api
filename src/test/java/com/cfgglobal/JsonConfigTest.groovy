package com.cfgglobal

import com.cfgglobal.test.config.json.JsonConfig
import com.cfgglobal.test.util.Q
import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Path

import spock.lang.Specification
import spock.lang.Unroll

class JsonConfigTest extends Specification {
    def jsonConfig = new JsonConfig()

    def "FirstLevel"() {
        given:
        EntityPath role = Q.user
        when:
        List<Path> paths = JsonConfig.Companion.firstLevel(role, "role")
        Q.role
        then:
        println paths

    }


    @Unroll
    def "#uri endpoint is #endpoint"() {
        expect:

        JsonConfig.Companion.getRootEndpoint(uri) == endpint

        where:
        uri                       || endpint
        //  "/v1/payer/111/payee/1" || "payee"
        "/v1/payer/111/payee"     || "payee"
        "/v1/payer/1"             || "payer"
        "/v1/payer"               || "payer"
        "/v1/user/application"        || "user"
        "/v1/transaction/1/split" || "transaction"
    }

}
