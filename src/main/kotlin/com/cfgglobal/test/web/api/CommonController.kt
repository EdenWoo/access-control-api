package com.cfgglobal.test.web.api;

import arrow.core.Option
import arrow.core.ev
import arrow.core.getOrElse
import arrow.core.monad
import arrow.data.Try
import arrow.syntax.collections.firstOption
import arrow.syntax.option.toOption
import arrow.typeclasses.binding
import com.cfgglobal.test.service.base.BaseService
import com.cfgglobal.test.vo.Filter
import com.cfgglobal.test.web.base.BaseController
import com.github.leon.extentions.remainLastIndexOf
import com.github.leon.security.ApplicationProperties
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
) : BaseController() {

    @RequestMapping(value = ["/{entity}/{f}/{v}"], method = [RequestMethod.HEAD])
    fun existenceCheck(@PathVariable entity: String, @PathVariable f: String, @PathVariable v: String): ResponseEntity<String> {
        val service = context.getBean("${entity}Service") as BaseService<*, *>
        return Option.monad().binding {
            val field = f.toOption().bind()
            val value = v.toOption().bind()
            val filter = Filter().addCondition(field, value, Filter.OPERATOR_EQ)
            val result = Try { service.findByFilter(filter) }.toOption().bind()
            val status = when (result.size) {
                0 -> 200
                else -> 409
            }
            ResponseEntity.status(status).body(field)
        }.ev().getOrElse { ResponseEntity.badRequest().body(f + v) }
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