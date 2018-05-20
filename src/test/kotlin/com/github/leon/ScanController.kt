package com.github.leon

import arrow.core.getOrElse
import arrow.syntax.collections.firstOption
import com.github.leon.classpath.ClassSearcher
import org.springframework.web.bind.annotation.*
import java.util.regex.Pattern


fun main(args: Array<String>) {
    val pattern = Pattern.compile("\\{[a-z]*}")
    val entities: List<Class<out Any>> = ClassSearcher.of(Any::class.java).search()
    val controllers = entities.filter {
        it.isAnnotationPresent(RestController::class.java)
    }
    val list = listOf(
            GetMapping::class.java,
            PostMapping::class.java,
            DeleteMapping::class.java,
            PutMapping::class.java)

    controllers.forEach { controller ->
        val apiMethods = controller.methods.filter { method ->
            list.any { mappingAnnotation -> method!!.isAnnotationPresent(mappingAnnotation) }
        }
        apiMethods.forEach {
            var methodName = ""
            var url = "${controller.getDeclaredAnnotation(RequestMapping::class.java).value.first()}/"
            it.declaredAnnotations.forEach {
                when (it) {
                    is GetMapping -> {
                        methodName = "GET"
                        url += it.value.firstOption().getOrElse { "" }
                        url = pattern.matcher(url).replaceAll("[\\\\d]+")
                    }
                    is PostMapping -> {
                        methodName = "POST"
                        url += it.value.firstOption().getOrElse { "" }
                        url = pattern.matcher(url).replaceAll("[\\\\d]+")
                    }
                    is DeleteMapping -> {
                        methodName = "DELETE"
                        url += it.value.firstOption().getOrElse { "" }
                        url = pattern.matcher(url).replaceAll("[\\\\d]+")
                    }
                    is PutMapping -> {
                        methodName = "PUT"
                        url += it.value.firstOption().getOrElse { "" }
                        url = pattern.matcher(url).replaceAll("[\\\\d]+")
                    }
                }
            }
            print("name ${it.name}")
            print(" method  ${methodName}")
            print(" url ${url}")
            println()
        }
    }

}