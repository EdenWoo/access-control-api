package com.cfgglobal.test.config.json

import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.data.Try
import arrow.data.getOrElse
import arrow.syntax.collections.firstOption
import arrow.syntax.option.toOption
import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import com.github.leon.security.ApplicationProperties
import com.google.common.base.CaseFormat
import com.google.common.base.CaseFormat.*
import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.ListPath
import io.vavr.Tuple
import io.vavr.Tuple2
import org.apache.commons.lang3.StringUtils
import org.joor.Reflect
import java.lang.reflect.Modifier
import java.text.MessageFormat


class JsonConfig {
    var items = mutableListOf<JsonConfigItem>()

    fun include(type: Class<*>, vararg include: Path<*>): JsonConfig {
        val jsonConfigItems = items.firstOption { e -> e.type == type }
        when (jsonConfigItems) {
            is Some -> jsonConfigItems.get().include.addAll(include)
            else -> items.add(JsonConfigItem(type = type, include = include.toMutableList()))
        }
        return this
    }

    fun exclude(type: Class<*>, vararg exclude: Path<*>): JsonConfig {
        val jsonConfigItems = items.firstOption { e -> e.type == type }
        when (jsonConfigItems) {
            is Some -> jsonConfigItems.get().exclude.addAll(exclude)
            else -> items.add(JsonConfigItem(type = type, exclude = exclude.toMutableList()))
        }
        return this
    }


    fun end() {
        config.set(this)
    }

    companion object {


        private val config = ThreadLocal.withInitial<JsonConfig> { null }

        fun start(): JsonConfig {
            return JsonConfig()
        }

        fun get(): Option<JsonConfig> {
            return Option.fromNullable(config.get())
        }

        fun endpoints(endpoint: String): Option<Tuple2<Class<*>, EntityPathBase<*>>> {
            return ApplicationProperties.entityScanPackage.toList()
                    .map { packageName ->
                        Option.fromNullable(endpoint)
                                .map { e ->
                                    val name = LOWER_HYPHEN.to(UPPER_CAMEL, e)
                                    Tuple.of(packageName + "." + name, packageName + ".Q" + name)
                                }
                                .flatMap { e ->
                                    Try {
                                        e.map(
                                                { e1 -> Reflect.on(e1).get<Any>() as Class<*> }
                                        ) { e2 ->
                                            Reflect.on(e2).create(StringUtils.substringAfterLast(e2, ".Q")
                                                    .toLowerCase()).get<Any>() as EntityPathBase<*>
                                        }
                                    }.toOption()
                                }
                    }.firstOption { it.isDefined() }
                    .getOrElse { Option.empty() }


        }

        fun firstLevelPath(role: EntityPathBase<*>): List<Path<*>> {

            var paths: List<Path<*>> = role.javaClass.declaredFields.toList()
                    .filter { e -> !Modifier.isPrivate(e.modifiers) }
                    .filter { e -> !ListPath::class.java.isAssignableFrom(e.type) }
                    .filter { e -> !EntityPath::class.java.isAssignableFrom(e.type) }
                    .map { e -> Try { e.get(role) as Path<*> }.get() }
            return paths
        }

        fun firstLevel(clazz: Class<*>): List<String> {

            return clazz.declaredFields.toList()
                    .filter { e -> !List::class.java.isAssignableFrom(e.type) }
                    .filter { e -> !BaseEntity::class.java.isAssignableFrom(e.type) }
                    .map { it.name }
        }

        fun create(uri: String, fields: String?, embedded: String?): Option<JsonConfig> {
            val embeddedEntity = Option.fromNullable(embedded)
                    .map { it.split(",").toList() }
                    .getOrElse { emptyList() }
                    .filter { StringUtils.isNotBlank(it) }
                    .map { e -> e.split("\\.").toList() }
                    .filter { it.isNotEmpty() }
            val endpoint = JsonConfig.getRootEndpoint(uri)

            val rootElement = endpoints(endpoint)
            if (rootElement.isEmpty()) {
                return Option.empty()
            }
            val jsonConfig = JsonConfig.start()
            val fieldsInRequest = getFields(rootElement.get()._1, fields)
            jsonConfig.include(rootElement.get()._1,
                    *fieldsInRequest[rootElement.get()._1].toOption().getOrElse { JsonConfig.firstLevelPath(rootElement.get()._2) }.toTypedArray())

            if (!embeddedEntity.isEmpty()) {
                checkEmbedded(embeddedEntity)
                embeddedEntity.sortedBy { it.size }
                        .forEach { e ->
                            if (e.size < 2) {
                                // rootEntity embeddedNode:String
                                val embeddedNode = e.first()
                                addEmbedded(jsonConfig, fieldsInRequest, rootElement, embeddedNode)
                            } else {
                                //去除最后2级，倒数第二级已经在之前的循环中加入了第一层，只需要把嵌套节点追加进去，最后一层add first level
                                val lastNode = e.last()
                                val lastParentNode = e.dropLast(1).last()
                                var parentElement: Option<Tuple2<Class<*>, EntityPathBase<*>>> = endpoints(lastParentNode)
                                if (parentElement.isEmpty() && lastParentNode.endsWith("s")) {
                                    parentElement = endpoints(lastParentNode.substring(0, lastParentNode.length - 1))
                                }
                                addEmbedded(jsonConfig, fieldsInRequest, parentElement, lastNode)
                            }


                        }
            }
            return Option.fromNullable(jsonConfig)
        }

        private fun addEmbedded(jsonConfig: JsonConfig, fieldsInRequest: Map<Class<*>, List<Path<*>>>, rootElement: Option<Tuple2<Class<*>, EntityPathBase<*>>>, embeddedNode: String) {

            val rootEntity = rootElement.get()._2

            val embeddedEntityPath = Try {
                Reflect.on(rootEntity).get<Any>(LOWER_HYPHEN.to(LOWER_CAMEL, embeddedNode)) as Path<*>
            }.getOrElse {
                if (rootEntity.type === User::class.java) {//Special process for User class
                    val userClazz = Reflect.on(ApplicationProperties.myUserClass).get<Class<*>>()
                    Reflect.on(JsonConfig.toQ(userClazz)).get(LOWER_HYPHEN.to(LOWER_CAMEL, embeddedNode))
                } else {
                    null!!
                }
            }
                    ?: throw IllegalArgumentException(MessageFormat.format(("Invalid embedded [{0}],does not exist on entity [{1}],avaliable embedded [{2}]." +
                            "Metadata is based on QueryDSL's Q object, not javabean. " +
                            "Run `gradle clean build` to generate QueryDSL Q Object."), embeddedNode, rootEntity.toString(), getAvailableEmbeddedPaths(rootEntity)
                            .map { e -> LOWER_CAMEL.converterTo(LOWER_HYPHEN).convert(e) }.joinToString()))

            jsonConfig.include(rootElement.get()._1, embeddedEntityPath) //追加？

            val nextEntity = endpoints(getNext(embeddedEntityPath))
            val include = fieldsInRequest.get(nextEntity.get()._1).toOption().getOrElse { JsonConfig.firstLevelPath(nextEntity.get()._2) }
            jsonConfig.include(nextEntity.get()._1, *include.toTypedArray())


        }

        private fun getAvailableEmbeddedPaths(rootEntity: EntityPath<*>): List<String> {
            return rootEntity.javaClass.fields.toList()
                    .filter { re -> ListPath::class.java.isAssignableFrom(re.type) || EntityPath::class.java.isAssignableFrom(re.type) }
                    .map { it.name }
        }


        private fun getNext(embeddedEntityPath: Path<*>): String {
            var next: String
            if (embeddedEntityPath is ListPath<*, *>) {
                next = (Reflect.on(embeddedEntityPath).get<Any>("elementType") as Class<*>).simpleName
            } else {
                next = embeddedEntityPath.type.simpleName
            }

            next = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, next)
            return next
        }

        private fun checkEmbedded(embeddedEntity: List<List<String>>) {
            //payees,payees.bankaccounts, payees.attachments 父亲节点必须手动加入
            embeddedEntity.forEach { e ->
                if (e.size >= 2) {
                    for (i in 1 until e.size) {
                        val parentNode = e.subList(0, i)
                        if (!embeddedEntity.contains(parentNode)) {
                            throw IllegalArgumentException(MessageFormat.format("Invalid embedded [{1}].[{0}] should be added for embedded [{1}] ",
                                    parentNode.joinToString("."), e.joinToString(".")))
                        }
                    }
                }
            }
        }

        fun toQ(clazz: Class<*>): Path<*> {
            return ApplicationProperties.entityScanPackage.toList()
                    .map { packageName ->
                        val name = packageName + ".Q" + clazz.simpleName
                        Try { Reflect.on(name.replace("////".toRegex(), ".")).create(StringUtils.substringAfterLast(name, ".Q")).get<Any>() as EntityPathBase<*> }
                    }.first { it.isSuccess() }.get()


        }

        fun getDto(aClass: Class<*>): Option<Class<*>> {
            val name = "com.cfgglobal.ccfx.web.api.vo." + aClass.simpleName + "Dto"
            return Try { Reflect.on(name).get<Any>() as Class<*> }.toOption()
        }

        fun getRootEndpoint(u: String): String {
            val uri = u.replace("/v1/", "")
            return when (StringUtils.countMatches(uri, "/")) {
                0 -> uri
                1 -> StringUtils.substringBefore(uri, "/")
                2 -> StringUtils.substringAfterLast(uri, "/")
                3 -> StringUtils.substringAfterLast(uri, "/")
                else -> throw IllegalArgumentException()
            }


        }

        fun getFields(clazz: Class<*>, fields: String?): Map<Class<*>, List<Path<*>>> {

            val map = fields.toOption()
                    .map { e -> e.split(",") }
                    .map { it.toList() }
                    .getOrElse { emptyList() }
                    .map { e ->
                        val results = e.split("\\.")
                        when (results.size) {
                            1 -> {
                                val field = results[0]
                                Pair(clazz, listOf(MockPath.create(field) as Path<*>))
                            }
                            2 -> {
                                val className = results[0]
                                val field = results[1]
                                val nestedClass = clazz.declaredFields.toList()
                                        .first { e2 -> e2.name == className }
                                        .type
                                Pair(nestedClass, listOf(MockPath.create(field) as Path<*>))
                            }
                            else -> {
                                throw IllegalArgumentException()
                            }
                        }

                    }
            return map.toMap()

        }
    }


}
