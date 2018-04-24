package com.github.leon.aci.config.json

import arrow.core.Try
import com.github.leon.aci.domain.User
import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter
import com.fasterxml.jackson.databind.ser.FilterProvider
import com.fasterxml.jackson.databind.ser.PropertyFilter
import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter

import com.github.leon.aci.security.ApplicationProperties
import org.apache.commons.lang3.StringUtils
import org.hibernate.collection.spi.PersistentCollection
import org.hibernate.proxy.HibernateProxy
import org.joor.Reflect


@JsonFilter("JacksonFilter")
class JacksonJsonFilter : FilterProvider() {

    internal var includeMap: MutableMap<Class<*>, MutableSet<String>> = mutableMapOf()
    internal var filterMap: MutableMap<Class<*>, MutableSet<String>> = mutableMapOf()

    fun include(type: Class<*>, fields: Array<String>) {
        addToMap(includeMap, type, fields)
    }

    fun exclude(type: Class<*>, fields: Array<String>) {
        addToMap(filterMap, type, fields)
    }

    private fun addToMap(map: MutableMap<Class<*>, MutableSet<String>>, type: Class<*>, fields: Array<String>) {
        val fieldSet: MutableSet<String> = map.getOrDefault(type, mutableSetOf())
        fieldSet.addAll(fields)
        map[type] = fieldSet
    }

    override fun findFilter(filterId: Any): BeanPropertyFilter {
        throw UnsupportedOperationException("Access to deprecated filters not supported")
    }

    override fun findPropertyFilter(filterId: Any, valueToFilter: Any?): PropertyFilter {

        return object : SimpleBeanPropertyFilter() {

            @Throws(Exception::class)
            override fun serializeAsField(pojo: Any, jgen: JsonGenerator, prov: SerializerProvider, writer: PropertyWriter) {
                val name = writer.name
                if (apply(pojo.javaClass, name)) {
                    /* System.out.println(valueToFilter.getClass());
                    Object obj = Reflect.on(pojo).get(name);
                    if (isLazy(obj)) {
                        System.out.println("lazy");
                        return;
                    }*/

                    writer.serializeAsField(pojo, jgen, prov)
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, prov)
                }
            }
        }
    }

    fun apply(type: Class<*>, name: String): Boolean {
        var type = type
        val simpleName = type.simpleName
        if (simpleName.endsWith("Dto")) {
            type = ApplicationProperties.entityScanPackage.toList()
                    .map { p -> Try { Reflect.on(p + "." + StringUtils.substringBefore(simpleName, "Dto")).get<Any>() as Class<*> } }
                    .first { it.isSuccess() }
                    .get()
        }
        if (type.superclass == User::class.java && includeMap[type.superclass] != null) {
            type = User::class.java
        }
        val includeFields = includeMap[type]
        val filterFields = filterMap[type]
        if (!includeFields!!.isEmpty() && includeFields.contains(name)) {
            return true
        } else if (!filterFields!!.isEmpty() && !filterFields.contains(name)) {
            return true
        } else if (!includeFields.isEmpty() && !filterFields.isEmpty()) {
            //return true;
        }
        return false
    }

    fun isLazy(value: Any): Boolean {

        if (value is HibernateProxy) {//hibernate代理对象
            val initializer = value.hibernateLazyInitializer
            if (initializer.isUninitialized) {
                return true
            }
        } else if (value is PersistentCollection) {//实体关联集合一对多等
            if (!value.wasInitialized()) {
                return true
            }
            val `val` = value.value ?: return true
        }
        return false
    }

}