package com.cfgglobal.test.web.api;

import arrow.core.Option
import arrow.core.ev
import arrow.core.getOrElse
import arrow.core.monad
import arrow.data.Try
import arrow.syntax.option.toOption
import arrow.typeclasses.binding
import com.cfgglobal.test.service.base.BaseService
import com.cfgglobal.test.vo.Filter
import com.cfgglobal.test.web.base.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

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
            val result = Try{service.findByFilter(filter)}.toOption().bind()
            val status = when (result.size) {
                0 -> 404
                else -> 200
            }
            ResponseEntity.status(status).body(field)
        }.ev().getOrElse { ResponseEntity.badRequest().body(f + v) }
    }
}