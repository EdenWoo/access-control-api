package com.cfgglobal.test.service

import com.cfgglobal.test.dao.RuleDao
import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.domain.RolePermission
import io.vavr.Tuple
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GeneratorService {
    @Autowired
    private val ruleDao: RuleDao? = null

    fun genPermission(entity: BaseEntity): List<Permission> {
        val regex = "([a-z])([A-Z]+)"
        val replacement = "$1-$2"
        val name = entity.javaClass.simpleName.replace(regex.toRegex(), replacement).toLowerCase()
        val endPoint = "/" + name.toLowerCase()

        val list = listOf(
                Tuple.of("Index " + name, "GET", "/v" + DIGIT + endPoint),
                Tuple.of("Create " + name, "POST", "/v" + DIGIT + endPoint),
                Tuple.of("Read " + name, "GET", "/v$DIGIT$endPoint/$DIGIT"),
                Tuple.of("Update " + name, "PUT", "/v$DIGIT$endPoint/$DIGIT"),
                Tuple.of("Update " + name, "PATCH", "/v$DIGIT$endPoint/$DIGIT"),
                Tuple.of("Delete " + name, "DELETE", "/v$DIGIT$endPoint/$DIGIT"))

        return list.map { e ->
            Permission(authKey = e._1,
                    entity = name,
                    httpMethod = e._2,
                    authUris = e._3)
        }
    }

    fun assignPermission(permissions: List<Permission>, permissionRule: String): List<RolePermission> {
        val rule = ruleDao!!.findByName(permissionRule)
                .getOrElseThrow { IllegalArgumentException(DEFAULT_RULE_NAME) }
        return permissions.map { permission ->
            RolePermission(permission = permission,
                    rules = mutableListOf(rule))
        }
    }

    companion object {

        val DEFAULT_RULE_NAME = "admin"
        val DIGIT = "[\\d]+"
    }

}