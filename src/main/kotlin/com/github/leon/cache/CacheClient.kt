package com.github.leon.cache


import java.util.function.Supplier

interface CacheClient {

    operator fun <T> get(key: String): T

    operator fun <T> get(key: String, supplier: Supplier<T>): T

    operator fun <T> get(key: String, supplier: Supplier<T>, expire: Long): T

    operator fun set(key: String, value: Any)


    operator fun set(key: String, expire: Long, value: Any)

    fun deleteByKey(key: String)

    fun deleteByPattern(key: String)

    fun keys(key: String): Set<String>


}
