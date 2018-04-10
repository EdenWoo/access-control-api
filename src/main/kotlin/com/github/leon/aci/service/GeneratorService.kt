package com.github.leon.aci.service

import arrow.core.None
import arrow.core.Some
import arrow.core.toOption
import com.github.leon.aci.dao.RuleDao
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.domain.Permission
import com.github.leon.aci.domain.RolePermission
import com.github.leon.aci.domain.Rule
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
                Triple("Index $name", "GET", "/v$DIGIT$endPoint"),
                Triple("Create $name", "POST", "/v$DIGIT$endPoint"),
                Triple("Read $name", "GET", "/v$DIGIT$endPoint/$DIGIT"),
                Triple("Update $name", "PUT", "/v$DIGIT$endPoint/$DIGIT"),
                Triple("Update $name", "PATCH", "/v$DIGIT$endPoint/$DIGIT"),
                Triple("Delete $name", "DELETE", "/v$DIGIT$endPoint/$DIGIT"))

        return list.map { e ->
            Permission(authKey = e.first,
                    entity = name,
                    httpMethod = e.second,
                    authUris = e.third)
        }
    }

    fun assignPermission(permissions: List<Permission>, permissionRule: String): List<RolePermission> {
        val ruleOpt = ruleDao!!.findByName(permissionRule).toOption()
        val rule: Rule = when (ruleOpt) {
            is Some -> ruleOpt.t
            None -> throw IllegalArgumentException(DEFAULT_RULE_NAME)
        }

        return permissions.map { permission ->
            RolePermission(permission = permission,
                    rules = mutableListOf(rule))
        }
    }

    companion object {

        const val DEFAULT_RULE_NAME = "admin"
        const val DIGIT = "[\\d]+"
    }

}
