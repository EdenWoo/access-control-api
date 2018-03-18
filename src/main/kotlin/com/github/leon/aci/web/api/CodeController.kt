package com.github.leon.aci.web.api

import com.github.leon.aci.service.PermissionService
import com.github.leon.freemarker.FreemarkerBuilderUtil
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.IOException

@Controller
@RequestMapping("/v1/code")
class CodeController {


    @Value("\${app.user-class}")
    lateinit var userClass: String

    @Autowired
    private val permissionService: PermissionService? = null

    @Autowired
    private val freemarkerBuilderUtil: FreemarkerBuilderUtil? = null

    private val permissionMap: List<Map<String, String>>
        get() = permissionService!!.findAll()
                .map { (_, authKey) ->
                    mapOf("key" to authKey.split(" ").joinToString(separator = "_").toUpperCase().replace("-", "_"),
                            "value" to  authKey)
                }

    @GetMapping(value = ["/permission_constant"], produces = ["text/html;charset=UTF-8"])
    @ResponseBody
    @Throws(IOException::class)
    fun init(modelMap: ModelMap): ResponseEntity<String> {
        val list = permissionMap
        val map = java.util.HashMap<String, Any>()
        map["permissions"] = list.distinctBy { e -> e["key"] }
        modelMap.putAll(map)
        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/permission.ftl", map)!!)
    }


    @GetMapping(value = ["/permission-constant_model_ts"], produces = ["text/html;charset=UTF-8"])
    @ResponseBody
    @Throws(IOException::class)
    fun model(modelMap: ModelMap): ResponseEntity<String> {


        val list = permissionMap
        val map = java.util.HashMap<String, Any>()
        map["permissions"] = list.distinctBy { e -> e["key"] }
        modelMap.putAll(map)
        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/permission-constant_model_ts.ftl", map)!!)
    }


    @GetMapping(value = ["/entity-model_ts"], produces = ["text/html;charset=UTF-8"])
    @ResponseBody
    @Throws(IOException::class)
    fun model(entityName: String, modelMap: ModelMap): ResponseEntity<String> {
        var modelMap = modelMap
        modelMap = codeFetch(entityName, modelMap)

        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/entity-model_ts.ftl", modelMap)!!)

    }

    @GetMapping(value = ["/entity-list-component_ts"], produces = ["text/html;charset=UTF-8"])
    @ResponseBody
    @Throws(IOException::class)
    fun component(entityName: String, modelMap: ModelMap): ResponseEntity<String> {
        var modelMap = modelMap
        modelMap = codeFetch(entityName, modelMap)

        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/entity-list-component_ts.ftl", modelMap)!!)

    }

    @GetMapping(value = ["/entity-form_ts"], produces = ["text/html;charset=UTF-8"])
    @ResponseBody
    @Throws(IOException::class)
    fun form(entityName: String, modelMap: ModelMap): ResponseEntity<String> {
        var modelMap = modelMap

        modelMap = codeFetch(entityName, modelMap)
        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/entity-form_ts.ftl", modelMap)!!)

    }

    @GetMapping(value = ["/entity-form-component_ts"], produces = arrayOf("text/html;charset=UTF-8"))
    @ResponseBody
    @Throws(IOException::class)
    fun formComponent(entityName: String, modelMap: ModelMap): ResponseEntity<String> {
        var modelMap = modelMap

        modelMap = codeFetch(entityName, modelMap)
        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/entity-form-component_ts.ftl", modelMap)!!)
    }

    @GetMapping(value = ["/entity-list_ts"], produces = arrayOf("text/html;charset=UTF-8"))
    @ResponseBody
    @Throws(IOException::class)
    fun list(entityName: String, modelMap: ModelMap): ResponseEntity<String> {
        var modelMap = modelMap

        modelMap = codeFetch(entityName, modelMap)
        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/entity-list_ts.ftl", modelMap)!!.toString())

    }

    @GetMapping(value = ["/entity-service_ts"], produces = arrayOf("text/html;charset=UTF-8"))
    @ResponseBody
    @Throws(IOException::class)
    fun entityService(entityName: String, modelMap: ModelMap): ResponseEntity<String> {
        var modelMap = modelMap

        modelMap = codeFetch(entityName, modelMap)
        return ResponseEntity.ok(freemarkerBuilderUtil!!.build("/code/entity-service_ts.ftl", modelMap)!!.toString())

    }

    private fun codeFetch(entityName: String?, modelMap: ModelMap): ModelMap {
        if (entityName != null) {
            val clazz = Reflect.on(userClass).get<Class<*>>()
            val name = clazz.`package`.name + "." + entityName
            println("entity:  $name")

            modelMap["entity"] = getReplace(name).toLowerCase()

            var reflect: Reflect?
            try {
                reflect = Reflect.on(name)
            } catch (e: Exception) {
                reflect = Reflect.on("com.cfgglobal.ccfx.domain.$entityName")
            }

            val entityClass = reflect!!.get<Class<*>>()
            val fields = entityClass.declaredFields
                    .map { e ->
                        val mapOf = mapOf(
                                "name" to getReplace(e.name),
                                "type" to getTypeReplace(e.getType().getSimpleName())
                        )
                        mapOf
                    }
            modelMap["fields"] = fields
            println(modelMap)
        }
        return modelMap
    }


    private fun getReplace(e: String): String {
        val clazz = Reflect.on(userClass).get<Class<*>>()
        return e.replace(clazz.`package`.name + ".", "").replace("com.cfgglobal.ccfx.domain.", "")
    }

    private fun getTypeReplace(e: String): String {
        return e.replace("LocalDate", "number")
                .replace("String", "string")
                .replace("int", "number")
                .replace("Integer", "number")
                .replace("ZonedDateTime", "string")
                .replace("byte", "number")
                .replace("long", "number")
                .replace("Boolean", "boolean")
    }

}