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

fun <T> T?.ok():ResponseEntity<T> {
    return  ResponseEntity.ok(this)
}



