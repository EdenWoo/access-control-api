package com.github.leon.template.permissions


import com.github.leon.template.permissions.bean.*
import com.google.common.base.CaseFormat
import java.io.File
import java.io.FileInputStream
import java.lang.UnsupportedOperationException

import java.sql.*
import java.util.Properties
import java.sql.ResultSet


var conn: Connection? = null



fun executeMySQLQuery() {
    var stmt: Statement? = null
    var resultset: ResultSet? = null

    try {
        /* val roleList = getRoles(getResultSet("role", stmt, resultset))
         val ruleList = getRules(getResultSet("rule", stmt, resultset))
         val permissionList = getPermissions("address")
         val rolePermissionList = getRolePermissions(roleList, permissionList)
         val rolePermissionRuleList = getRolePermissionRule(rolePermissionList, ruleList)
         val permissionSqlList = getPermissionSql(permissionList)
         val rolePermissionSqlList = getRolePermissionSql(rolePermissionList)
         val rolePermissionRuleSqlList = getRolePermissionRuleSql(rolePermissionRuleList)
         */

    } catch (ex: SQLException) {
        // handle any errors
        ex.printStackTrace()
    } finally {
        // release resources
        if (resultset != null) {
            try {
                resultset.close()
            } catch (sqlEx: SQLException) {
            }

            resultset = null
        }

        if (stmt != null) {
            try {
                stmt.close()
            } catch (sqlEx: SQLException) {
            }

            stmt = null
        }

        if (conn != null) {
            try {
                conn!!.close()
            } catch (sqlEx: SQLException) {
            }

            conn = null
        }
    }
}

fun getConnection() {

    val rootPath = Thread.currentThread().contextClassLoader.getResource("")!!.path
    val connectionConfigPath = "${File(rootPath).parent}/resources/generator/local.properties"
    val connectionProps = Properties()
    connectionProps.load(FileInputStream(connectionConfigPath))

    val jdbcUser = connectionProps.getProperty("jdbcUser")
    val jdbcPassword = connectionProps.getProperty("jdbcPassword")
    val jdbcDriver = connectionProps.getProperty("jdbcDriver")
    val jdbcUrl = connectionProps.getProperty("jdbcUrl")

    connectionProps["user"] = jdbcUser
    connectionProps["password"] = jdbcPassword
    try {
        Class.forName(jdbcDriver).newInstance()
        conn = DriverManager.getConnection(jdbcUrl, connectionProps)

    } catch (ex: SQLException) {
        // handle any errors
        ex.printStackTrace()
    } catch (ex: Exception) {
        // handle any errors
        ex.printStackTrace()
    }
}

fun getResultSet(beanName: String, statement: Statement?, resultset: ResultSet?): ResultSet {
    var stmt = statement
    var res = resultset
    stmt = conn!!.createStatement()
    val sql = "SELECT * FROM $beanName;"
    res = stmt!!.executeQuery(sql)

    if (stmt.execute(sql)) {
        res = stmt.resultSet

    }
    return res
}

fun getRoles(resultset: ResultSet): List<TaskRole> {
    val roleList = mutableListOf<TaskRole>()
    while (resultset.next()) {
        val role = TaskRole()
        val roleFields = TaskRole::class.java.declaredFields
        roleFields.forEach {
            val name = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(it.name)

            val res = when (it.type) {
                java.lang.Long::class.java -> resultset.getLong(name)
                java.lang.String::class.java -> resultset.getString(name)
                else -> throw UnsupportedOperationException("${it.type}")
            }

            it.isAccessible = true
            it.set(role, res)
            it.isAccessible = false
        }
        roleList.add(role)
    }
    return roleList
}

fun getRules(resultset: ResultSet): List<TaskRule> {
    val ruleList = mutableListOf<TaskRule>()
    while (resultset.next()) {
        val rule = TaskRule()
        val ruleFileds = TaskRule::class.java.declaredFields
        ruleFileds.forEach {
            val name = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(it.name)

            val res = when (it.type) {
                java.lang.Long::class.java -> resultset.getLong(name)
                java.lang.String::class.java -> resultset.getString(name)
                else -> throw UnsupportedOperationException("${it.type}")
            }

            it.isAccessible = true
            it.set(rule, res)
            it.isAccessible = false
        }
        ruleList.add(rule)
    }
    return ruleList
}

fun getPermissions(entityName: String, baseId: Long): List<TaskPermission> {

    val permissionList = mutableListOf<TaskPermission>()
    permissionList.add(indexPermission(entityName))
    permissionList.add(createPermission(entityName))
    permissionList.add(readPermission(entityName))
    permissionList.add(updatePermission(entityName))
    permissionList.add(deletePermission(entityName))

    permissionList.mapIndexed { index, permission ->
        permission.id = (baseId * 100 + index.inc())
    }

    return permissionList

}

fun indexPermission(entityName: String): TaskPermission {
    val permission = TaskPermission()
    val endPoint = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(entityName)
    permission.version = 0
    permission.authKey = "Index $entityName"
    permission.authUris = "/v[\\\\d]+/$endPoint"
    permission.display = "Index $entityName"
    permission.entity = entityName
    permission.httpMethod = "GET"
    permission.creatorId = 1
    permission.modifierId = 1
    return permission
}

fun createPermission(entityName: String): TaskPermission {
    val endPoint = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(entityName)
    val permission = TaskPermission()
    permission.version = 0
    permission.authKey = "Create $entityName"
    permission.authUris = "/v[\\\\d]+/$endPoint"
    permission.display = "Create $entityName"
    permission.entity = entityName
    permission.httpMethod = "POST"
    permission.creatorId = 1
    permission.modifierId = 1
    return permission
}

fun readPermission(entityName: String): TaskPermission {
    val endPoint = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(entityName)
    val permission = TaskPermission()
    permission.version = 0
    permission.authKey = "Read $entityName"
    permission.authUris = "/v[\\\\d]+/$endPoint/[\\\\d]+"
    permission.display = "Read $entityName"
    permission.entity = entityName
    permission.httpMethod = "GET"
    permission.creatorId = 1
    permission.modifierId = 1
    return permission
}

fun updatePermission(entityName: String): TaskPermission {
    val endPoint = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(entityName)
    val permission = TaskPermission()
    permission.version = 0
    permission.authKey = "Update $entityName"
    permission.authUris = "/v[\\\\d]+/$endPoint/[\\\\d]+"
    permission.display = "Update $entityName"
    permission.entity = entityName
    permission.httpMethod = "PUT"
    permission.creatorId = 1
    permission.modifierId = 1
    return permission
}

fun deletePermission(entityName: String): TaskPermission {
    val endPoint = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert(entityName)
    val permission = TaskPermission()
    permission.version = 0
    permission.authKey = "Delete $entityName"
    permission.authUris = "/v[\\\\d]+/$endPoint/[\\\\d]+"
    permission.display = "Delete $entityName"
    permission.entity = entityName
    permission.httpMethod = "DELETE"
    permission.creatorId = 1
    permission.modifierId = 1
    return permission
}

fun getRolePermissions(taskRoleList: List<TaskRole>, permissionList: List<TaskPermission>): List<TaskRolePermission> {
    val rolePermissionList = mutableListOf<TaskRolePermission>()

    taskRoleList.map { role ->
        permissionList.map { permission ->
            val rolePermission = TaskRolePermission()
            rolePermission.version = 0
            rolePermission.creatorId = 1
            rolePermission.modifierId = 1
            rolePermission.roleId = role.id
            rolePermission.permissionId = permission.id
            rolePermissionList.add(rolePermission)
        }
    }

    rolePermissionList.map {
        it.id = (it.roleId.toString() + it.permissionId.toString()).toLong()
    }
    return rolePermissionList
}

fun getRolePermissionRule(taskRolePermissionList: List<TaskRolePermission>, taskRuleList: List<TaskRule>): List<TaskRolePermissionRule> {
    val rolePermissionRuleList = mutableListOf<TaskRolePermissionRule>()
    taskRolePermissionList.map { rolePermission ->
        listOf(taskRuleList.first()).map { rule ->
            val rolePermissionRule = TaskRolePermissionRule()
            rolePermissionRule.rolePermissionId = rolePermission.id
            rolePermissionRule.ruleId = rule.id
            rolePermissionRuleList.add(rolePermissionRule)
        }
    }

    return rolePermissionRuleList
}

fun getPermissionSql(permissionList: List<TaskPermission>): List<String> {
    val sqlList = mutableListOf<String>()
    permissionList.map { permission ->
        val sql = """
                INSERT INTO aci_permission (id, version, auth_key, auth_uris, display, entity, http_method, creator_id, modifier_id)
                VALUES (${permission.id},${permission.version},'${permission.authKey}','${permission.authUris}','${permission.display}','${permission.entity}','${permission.httpMethod}',${permission.creatorId}, ${permission.modifierId});
                """.trimIndent()

        sqlList.add(sql)
    }
    return sqlList
}

fun getRolePermissionSql(taskRolePermissionList: List<TaskRolePermission>): List<String> {
    val sqlList = mutableListOf<String>()
    taskRolePermissionList.map { rolePermission ->
        val sql = """
                INSERT INTO aci_role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
                VALUES (${rolePermission.id}, ${rolePermission.version}, ${rolePermission.creatorId}, ${rolePermission.modifierId}, ${rolePermission.permissionId}, ${rolePermission.roleId});
                """.trimIndent()

        sqlList.add(sql)
    }
    return sqlList
}

fun getRolePermissionRuleSql(rolePermissionRuleList: List<TaskRolePermissionRule>): List<String> {
    val sqlList = mutableListOf<String>()
    rolePermissionRuleList.map { rolePermissionRule ->
        val sql = """
                INSERT INTO aci_role_permission_rule (role_permission_id, rule_id)
                VALUES (${rolePermissionRule.rolePermissionId}, ${rolePermissionRule.ruleId});
                """.trimIndent()

        sqlList.add(sql)
    }
    return sqlList
}
