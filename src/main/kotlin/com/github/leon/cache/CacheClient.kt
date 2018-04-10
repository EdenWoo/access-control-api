package com.github.leon.cache


interface CacheClient {

    fun <T> get(key: String): T?

    fun <T> get(key: String, supplier: () -> T): T?

    fun <T> get(key: String, supplier: () -> T, expire: Long): T?

    fun set(key: String, value: Any)

    fun set(key: String, expire: Long, value: Any)

    fun deleteByKey(key: String)

    fun deleteByPattern(pattern: String)

    fun keys(key: String): Set<String>


}
