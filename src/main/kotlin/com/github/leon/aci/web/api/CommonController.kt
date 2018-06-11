package com.github.leon.aci.web.api

import arrow.core.*
import arrow.syntax.collections.firstOption
import arrow.typeclasses.binding
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.aci.service.base.BaseService
import com.github.leon.aci.vo.Condition
import com.github.leon.aci.vo.Filter
import com.github.leon.extentions.remainLastIndexOf
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class CommonController(
        @Autowired
        val context: ApplicationContext
) {

    @RequestMapping(value = ["/{entity}/{f}/{v}"], method = [RequestMethod.HEAD])
    fun existenceCheck(@PathVariable entity: String, @PathVariable f: String, @PathVariable v: String, id: Long?): ResponseEntity<String> {
        val service = context.getBean("${entity}Service") as BaseService<*, *>
        return Option.monad().binding {
            val field = f.toOption().bind()
            val value = v.toOption().bind()
            val filter = Filter(conditions = listOf(Condition(
                    fieldName = field,
                    value = value,
                    operator = Filter.OPERATOR_EQ
            )))
            id.toOption().forEach {
                filter.conditions = filter.conditions + Condition(fieldName = "id", value = id, operator = Filter.OPERATOR_NOT_EQ)
            }

            val result = Try { service.findByFilter(filter) }.toOption().bind()
            val status = when (result.size) {
                0 -> 200
                else -> 409
            }
            ResponseEntity.status(status).body(field)
        }.fix().getOrElse { ResponseEntity.status(200).body("") }
    }

    @GetMapping("/enum/{name}")
    fun enum(@PathVariable name: String): ResponseEntity<List<String>> {
        val enumClazz: Class<out Enum<*>> = ApplicationProperties.enumPackages
                .map { p -> Try { Reflect.on("$p.$name").get<Any>() as Class<out Enum<*>> } }
                .firstOption { it.isSuccess() }.get().get()

        val value = (Reflect.on(enumClazz).call("values").get() as Array<*>)
                .map { it.toString().remainLastIndexOf(".") }
                .toList()
        return ResponseEntity.ok(value)
    }


}