package com.github.leon.cache

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.Try
import arrow.core.toOption
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class RedisCacheClient : CacheClient {

    @Autowired
    private val template: RedisTemplate<String, Any>? = null


    override fun <T> get(key: String): T? {
        return template!!.opsForValue().get(key) as T?
    }

    override fun <T> get(key: String, supplier: () -> T): T? {
        val cache = Try { template!!.opsForValue().get(key) as T? }
        return when (cache) {
            is Try.Success -> cache.value.toOption()
            is Try.Failure -> {
                logger.error("Query key [{}] from cache failure", key, cache.exception.message)
                Option.empty()
            }
        }.getOrElse {
            val value =  supplier()
            logger.debug("Query key [{}] from cache success, value [{}] ", key, value)
            template!!.opsForValue().set(key, value)
            value
        }
    }

    override fun <T> get(key: String, supplier: () -> T, expire: Long): T? {
        val value = get(key, supplier)
        template!!.expire(key, expire, TimeUnit.SECONDS)
        return value
    }


    override fun set(key: String, value: Any) {
        template!!.opsForValue().set(key, value)
    }


    override fun set(key: String, expire: Long, value: Any) {
        set(key, value)
        template!!.expire(key, expire, TimeUnit.SECONDS)
    }

    override fun deleteByKey(key: String) {
        template!!.delete(key)
    }

    override fun deleteByPattern(pattern: String) {
        val keys = template!!.keys(pattern)
        for (key in keys) {
            logger.debug("Delete key [{}] in cache", key)
            template.delete(key)
        }
    }

    override fun keys(pattern: String): Set<String> {
        return template!!.keys(pattern)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(com.github.leon.cache.RedisCacheClient::class.java)
    }


}
