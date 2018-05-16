package com.github.leon.aci.web.api

import com.github.leon.aci.extenstions.ok
import com.github.leon.aci.extenstions.orElse
import com.github.leon.cache.CacheClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/v1/cache")
class CacheController(
        @Autowired
        val cacheClient: CacheClient

) {
    val log = LoggerFactory.getLogger(CacheController::class.java)!!
    @GetMapping
    fun get(key: String?): ResponseEntity<List<Map<String?,Any?>>> {

        return cacheClient.keys(key.orElse("*")).map {
            val value = cacheClient.get<Any>(it)
            log.debug("key $key , value $value")
            mapOf(key to value)
        }.ok()
    }

    @DeleteMapping("{key}")
    fun delete(@PathVariable key: String): ResponseEntity<*> {
        cacheClient.deleteByKey(key)
        return key.ok()
    }

}
