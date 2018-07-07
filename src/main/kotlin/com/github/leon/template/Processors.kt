package com.github.leon.template

import com.github.leon.generator.entity.CodeEntity
import com.github.leon.generator.entity.CodeProject
import com.github.leon.template.permissions.*
import java.sql.ResultSet
import java.sql.Statement

val projectPermissionProcessor: (CodeProject) -> Map<String, Any?> = {
    val allPermissionSqlList = mutableListOf<String>()
    val allRolePermissionSqlList = mutableListOf<String>()
    val allRolePermissionRuleSqlList = mutableListOf<String>()

    it.entities.forEach {

        getConnection()
        val stmt: Statement? = null
        val resultSet: ResultSet? = null
        val roleList = getRoles(getResultSet("aci_role", stmt, resultSet))
        val ruleList = getRules(getResultSet("aci_rule", stmt, resultSet))
        val permissionList = getPermissions(it.name, it.code.toLong())
        val rolePermissionList = getRolePermissions(roleList, permissionList)
        val rolePermissionRuleList = getRolePermissionRule(rolePermissionList, ruleList)
        val permissionSqlList = getPermissionSql(permissionList)
        val rolePermissionSqlList = getRolePermissionSql(rolePermissionList)
        val rolePermissionRuleSqlList = getRolePermissionRuleSql(rolePermissionRuleList)

        allPermissionSqlList.addAll(permissionSqlList)
        allRolePermissionSqlList.addAll(rolePermissionSqlList)
        allRolePermissionRuleSqlList.addAll(rolePermissionRuleSqlList)
    }

    mapOf(
            "projectExt" to "sql",
            "allPermissionSqlList" to allPermissionSqlList,
            "allRolePermissionSqlList" to allRolePermissionSqlList,
            "allRolePermissionRuleSqlList" to allRolePermissionRuleSqlList
    )
}


val entityPermissionProcessor: (CodeEntity) -> Map<String, Any?> = {
    getConnection()
    val stmt: Statement? = null
    val resultSet: ResultSet? = null
    val roleList = getRoles(getResultSet("role", stmt, resultSet))
    val ruleList = getRules(getResultSet("rule", stmt, resultSet))
    val permissionList = getPermissions(it.name, it.code.toLong())
    val rolePermissionList = getRolePermissions(roleList, permissionList)
    val rolePermissionRuleList = getRolePermissionRule(rolePermissionList, ruleList)
    val permissionSqlList = getPermissionSql(permissionList)
    val rolePermissionSqlList = getRolePermissionSql(rolePermissionList)
    val rolePermissionRuleSqlList = getRolePermissionRuleSql(rolePermissionRuleList)

    mapOf(
            "entityExt" to it.code.inc().toString(),
            "permissionSqlList" to permissionSqlList,
            "rolePermissionSqlList" to rolePermissionSqlList,
            "rolePermissionRuleSqlList" to rolePermissionRuleSqlList
    )
}

val excelProcessor: (CodeEntity) -> Map<String, Any?> = {

    fun serverProcessorFactor(jenkinsWorkspace: String,
                              jarFolder: String,
                              productionServer: String,
                              testServer: String): (CodeProject) -> Map<String, Any?> {

        val processor: (CodeProject) -> Map<String, Any?> = {
            mapOf(
                    "jenkinsWorkspace" to jenkinsWorkspace,
                    "jarFolder" to jarFolder,
                    "productionServer" to productionServer,
                    "testServer" to testServer
            )
        }

        return processor
    }


    val headerList = it.fields
                    .filter {
                        it.exportable == true
                    }
                    .map { """ "${it.header}" """ }

    val columnList = it.fields
            .filter {
                it.exportable == true
            }
            .map { """ "${it.column}" """ }


    val headerListStr = "listOf(${headerList.joinToString(",")})"
    val columnListStr = "listOf(${columnList.joinToString(",")})"

    mapOf(
            "excelList" to headerList,
            "columnList" to columnList,
            "headerListStr" to headerListStr,
            "columnListStr" to columnListStr
    )
}