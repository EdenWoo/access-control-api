package com.github.leon.aci.web.api

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.extenstions.orElse
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.cache.CacheClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/v1/cache")
class CacheController(
        @Autowired
        val cacheClient: CacheClient

) {
    val log = LoggerFactory.getLogger(CacheController::class.java)!!
    @GetMapping
    fun get(pattern: String?): ResponseEntity<List<Map<String, Any?>>> {
        val k = if (pattern == null) {
            "*"
        } else {
            "*$pattern*"
        }
        return cacheClient.keys(k.orElse("*")).map {
            var value = cacheClient.get<Any>(it)
            log.debug("pattern $it , value $value")
            when (value) {
                is BaseEntity -> {
                    value = "BaseEntity(Details omitted)"
                }
                is List<*> -> {
                    value = "List(Details omitted)"
                }
            }
            mapOf("key" to it, "value" to value)
        }.responseEntityOk()
    }

    @DeleteMapping
    fun delete(key: String, pattern: String): ResponseEntity<*> {
        cacheClient.deleteByKey(key)
        cacheClient.deleteByPattern(pattern)
        return key.responseEntityOk()
    }

}
