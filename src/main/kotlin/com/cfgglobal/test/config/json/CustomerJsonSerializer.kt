package com.cfgglobal.test.config.json

import arrow.core.None
import arrow.core.Some
import com.cfgglobal.test.domain.User
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.leon.security.ApplicationProperties
import com.querydsl.core.types.Path
import io.vavr.jackson.datatype.VavrModule
import org.joor.Reflect

class CustomerJsonSerializer {


    private val objectMapper = ObjectMapper()
    private val jacksonFilter = JacksonJsonFilter()

    fun filter(clazz: Class<*>?, include: MutableList<Path<*>>, exclude: MutableList<Path<*>>) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        // objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        if (clazz == null) {
            return
        }

        jacksonFilter.include(clazz, include.map { e -> e.metadata.name }.toTypedArray())

        jacksonFilter.exclude(clazz, exclude.map { e -> e.metadata.name }.toTypedArray())

        objectMapper.addMixIn(clazz, jacksonFilter.javaClass)
    }

    @Throws(JsonProcessingException::class)
    fun toJson(`object`: Any?): String {
        objectMapper.setFilterProvider(jacksonFilter)
        return objectMapper.writeValueAsString(`object`)
    }

    fun filter(json: JsonConfigItem) {
        val type = json.type
        val dto = JsonConfig.getDto(type!!)
        val include: MutableList<Path<*>> = json.include
        if (type == User::class.java) {
            val extendedUser = Reflect.on(ApplicationProperties.myUserClass).get<Class<*>>()
            include.addAll(JsonConfig.firstLevel(extendedUser).map({ MockPath.create(it) }))
        }
        when (dto) {
            is Some -> {
                val dtoFields = JsonConfig.firstLevel(dto.get()).map { MockPath.create(it) }
                include.addAll(dtoFields)
                this.filter(dto.get(), include, json.exclude)
                this.filter(json.type, include, json.exclude)
            }
            None -> this.filter(json.type, include, json.exclude)
        }
        if (type == User::class.java) {
            val extendedUser = Reflect.on(ApplicationProperties.myUserClass).get<Class<*>>()
            this.filter(extendedUser, include, json.exclude)
        }

    }
}