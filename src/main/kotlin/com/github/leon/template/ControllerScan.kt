package com.github.leon.template

import arrow.core.getOrElse
import arrow.syntax.collections.firstOption
import com.github.leon.classpath.ClassSearcher
import com.github.leon.extentions.println
import com.github.leon.template.permissions.bean.TaskPermission
import org.joor.Reflect
import org.springframework.web.bind.annotation.*
import java.util.regex.Pattern


fun main(args: Array<String>) {
    val entities: List<Class<out Any>> = ClassSearcher.of(Any::class.java).search()

    val taskPermission = entities.filter {
        it.isAnnotationPresent(RestController::class.java)
    }.flatMap { controller2Permission(it) }
            .filter { taskPermission ->
                val entityName = taskPermission.entity
                val existPermissionList = mutableListOf<Pair<Any, String>>(
                        Pair("GET", "/v1/$entityName/"),
                        Pair("GET", "/v1/$entityName/[\\d]+"),
                        Pair("POST", "/v1/$entityName/"),
                        Pair("PUT", "/v1/$entityName/[\\d]+"),
                        Pair("DELETE", "/v1/$entityName/[\\d]+")
                )
                val isNotExist = !existPermissionList.any {
                    it.first == taskPermission.httpMethod &&
                            it.second == taskPermission.authUris
                }
                isNotExist
            }

    taskPermission.forEach {
        it.println()
    }

}

fun controller2Permission(clazz: Class<*>): List<TaskPermission> {
    return clazz.declaredMethods.map { method ->
        val entityName = clazz.getDeclaredAnnotation(RequestMapping::class.java).value.first().substringAfterLast("/")
        val methodName = method.name

        val taskPermission = TaskPermission()
        taskPermission.version = 0
        taskPermission.entity = entityName
        taskPermission.creatorId = 1
        taskPermission.modifierId = 1
        taskPermission.authKey = "${methodName.capitalize()} $entityName"

        val pattern = Pattern.compile("\\{[a-z]*}") //TODO 大小写均可
        var authUrl = "${clazz.getDeclaredAnnotation(RequestMapping::class.java).value.first()}/"

        method.declaredAnnotations.forEach {
            when (it) {
                is GetMapping -> {
                    authUrl += it.value.firstOption().getOrElse { "" }
                    taskPermission.authUris = pattern.matcher(authUrl).replaceAll("[\\\\d]+")
                    taskPermission.httpMethod = "GET"
                }
                is PostMapping -> {
                    authUrl += it.value.firstOption().getOrElse { "" }
                    taskPermission.authUris = pattern.matcher(authUrl).replaceAll("[\\\\d]+")
                    taskPermission.httpMethod = "POST"
                }
                is PutMapping -> {
                    authUrl += it.value.firstOption().getOrElse { "" }
                    taskPermission.authUris = pattern.matcher(authUrl).replaceAll("[\\\\d]+")
                    taskPermission.httpMethod = "PUT"
                }
                is DeleteMapping -> {
                    authUrl += it.value.firstOption().getOrElse { "" }
                    taskPermission.authUris = pattern.matcher(authUrl).replaceAll("[\\\\d]+")
                    taskPermission.httpMethod = "DELETE"
                }
            }
        }
        taskPermission
    }
}

// 1 method reflection
// 2 Regex  捕获组   /{id}   /{/w+}

