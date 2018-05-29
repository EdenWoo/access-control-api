package com.github.leon.aci.extenstions

import arrow.core.getOrElse
import arrow.core.toOption
import org.springframework.http.ResponseEntity

fun Any?.println() {
    println(this)
}

fun Any?.print() {
    print(this)
}

fun <T> T?.orElse(default: T): T {
    return this.toOption().getOrElse { default }
}

fun <T> T?.responseEntityOk():ResponseEntity<T> {
    return  ResponseEntity.ok(this)
}

fun <T> T?.responseEntityBadRequest():ResponseEntity<T> {
    return  ResponseEntity.badRequest().body(this)
}







