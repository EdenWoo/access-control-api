package com.github.leon.aci.web.api

import arrow.core.Option
import arrow.core.ev
import arrow.core.getOrElse
import arrow.core.monad
import arrow.data.Try
import arrow.syntax.collections.firstOption
import arrow.syntax.option.toOption
import arrow.typeclasses.binding
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.aci.service.base.BaseService
import com.github.leon.aci.vo.Condition
import com.github.leon.aci.vo.Filter
import com.github.leon.ams.s3.AmazonService
import com.github.leon.ams.s3.UploadUtil
import com.github.leon.extentions.execCmd
import com.github.leon.extentions.remainLastIndexOf
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant

@RestController
@RequestMapping("/v1")
class CommonController(
        @Autowired
        val context: ApplicationContext,
        @Autowired
        val uploadUtil: UploadUtil,
        @Autowired
        val amazonService: AmazonService
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
        }.ev().getOrElse { ResponseEntity.status(200).body("") }
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


    @GetMapping("/dump")
    fun dump(): ResponseEntity<String> {
        val name = "/tmp/${Instant.now().epochSecond}.sql"
        val command = "mysqldump -uroot -proot collinson  role  permission rule role_permission role_permission_rule"
        val result = command.execCmd()
        Files.write(Paths.get(name), result)
        val awsFileName = "/v1/attachment/download?filename=${uploadUtil.write(File(name), "")}"
        return ResponseEntity.ok(awsFileName)
    }


    @GetMapping("/import")
    fun importSql(filename: String) {
        val file = amazonService.getFile(filename)
        val command = "mysql -uroot -proot"
        val command2 = "use collinson"
        val command3 = "source ${file.name}"
        val runtime = Runtime.getRuntime()

        val process = runtime.exec(command)
        val os = process.outputStream
        val writer = OutputStreamWriter(os)
        writer.write(command2 + "\r\n" + command3)
        writer.flush()
        writer.close()
        os.close()
    }
}