package com.github.leon.sysconfig.controller

import com.github.leon.aci.service.UserService
import com.github.leon.aci.web.base.BaseController
import com.github.leon.sysconfig.domain.SysConfig
import com.github.leon.sysconfig.service.SysConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/sys-config")

class SysConfigController(
        @Autowired
        val sysConfigService: SysConfigService,
        @Autowired
        val userService: UserService
) : BaseController<SysConfig, Long>() {

    @GetMapping("/can-trade")
    fun canTrade(): ResponseEntity<Map<*, *>> {
        val user = userService.findOne(loginUser.id!!)
        val canTrade = sysConfigService.canTrade(user)
        return ResponseEntity.ok(mapOf("canTrade" to canTrade))
    }

    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<SysConfig>> {
        return super.page(pageable, request)
    }

    @GetMapping("{id}")
    override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<SysConfig> {
        return super.findOne(id, request)
    }

    @PostMapping
    override fun saveOne(@RequestBody input: SysConfig, request: HttpServletRequest): ResponseEntity<*> {
        return super.saveOne(input, request)
    }

    @PutMapping
    override fun updateOne(@PathVariable id: Long, @RequestBody input: SysConfig, request: HttpServletRequest): ResponseEntity<*> {
        return super.updateOne(id, input, request)
    }

    @DeleteMapping("{id}")
    override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }
}

