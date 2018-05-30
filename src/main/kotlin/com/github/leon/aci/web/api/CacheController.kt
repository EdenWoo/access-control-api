package com.github.leon.aci.web.api

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.extenstions.pseudoPagination
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.cache.CacheClient
import com.github.leon.extentions.orElse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    fun get(pageable: Pageable, pattern: String?): ResponseEntity<Page<Map<String, Any?>>> {
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
        }.pseudoPagination(pageable).responseEntityOk()
    }

    @DeleteMapping
    fun delete(key: String?, pattern: String?): ResponseEntity<*> {
        if(key!=null) {
            cacheClient.deleteByKey(key)
        }
        if(pattern!=null) {
            cacheClient.deleteByPattern("*$pattern*")
        }
        return "success".responseEntityOk()
    }

}
