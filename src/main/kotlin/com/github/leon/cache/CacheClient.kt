package com.github.leon.cache


interface CacheClient {

    operator fun <T> get(key: String): T?

    operator fun <T> get(key: String, supplier: () -> T): T?

    operator fun <T> get(key: String, supplier: () -> T, expire: Long): T?

    operator fun set(key: String, value: Any)

    operator fun set(key: String, expire: Long, value: Any)

    fun deleteByKey(key: String)

    fun deleteByPattern(pattern: String)

    fun keys(key: String): Set<String>


}
