package com.github.leon.aci.extenstions

import arrow.syntax.collections.tail
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.extentions.orElse
import org.joor.Reflect
import org.springframework.http.ResponseEntity
import kotlin.reflect.full.memberFunctions

fun <T> T?.responseEntityOk(): ResponseEntity<T> {
    return ResponseEntity.ok(this)
}

fun <T> T?.responseEntityBadRequest(): ResponseEntity<T> {
    return ResponseEntity.badRequest().body(this)
}


fun <T> Any.copyFrom(newObj: T): T {

    if (newObj == null) {
        throw  IllegalArgumentException("from object is null")
    }
    if (newObj !is BaseEntity){
        throw  IllegalArgumentException("from Class is not  extended from BaseEntity")
    }
    val method = this::class.memberFunctions.first { it.name == "copy" }

    val parameterNames = method
            .parameters
            .tail()
            .mapIndexed { index, kParameter -> Pair(index, kParameter.name) }
    val oldValues = parameterNames.map {
        val v = Reflect.on(this).get<Any?>(it.second)
        v
    }

    val newValues = parameterNames.map {
        Reflect.on(newObj).get<Any?>(it.second)
    }
    val mergedValues = oldValues.zip(newValues).map { it.second.orElse(it.first) }
    val payerNew = method.call(this, *mergedValues.toTypedArray())
    val fields = BaseEntity::class.java.declaredFields
    fields.filter { it.name != "serialVersionUID" && it.name != "Companion" }
            .forEach {
                val oldV = Reflect.on(this).get<Any>(it.name)
                Reflect.on(payerNew).set(it.name, oldV)
            }
    return payerNew as T
}





