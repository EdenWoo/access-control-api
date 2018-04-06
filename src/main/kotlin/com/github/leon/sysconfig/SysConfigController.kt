package com.github.leon.sysconfig

import com.github.leon.aci.service.UserService
import com.github.leon.aci.web.base.BaseController
import com.github.leon.bean.JpaBeanUtil
import org.springframework.beans.factory.annotation.Autowired
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
) : BaseController() {

    @GetMapping("/can-trade")
    fun canTrade(): ResponseEntity<Map<*, *>> {
        val user = userService.findOne(loginUser.id!!)
        val canTrade = sysConfigService.canTrade(user)
        return ResponseEntity.ok(mapOf("canTrade" to canTrade))
    }

    @GetMapping
    fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<*> {
        val page = sysConfigService.findBySecurity(request.method, request.requestURI, request.parameterMap, pageable)
        return ResponseEntity.ok(page)
    }

    @GetMapping("{id}")
    operator fun get(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<SysConfig> {
        return ResponseEntity.ok(sysConfigService.findOneBySecurity(id, request.method, request.requestURI))
    }

    @PostMapping
    fun save(@RequestBody sysConfig: SysConfig, request: HttpServletRequest): ResponseEntity<SysConfig> {
        return ResponseEntity.ok(sysConfigService.saveBySecurity(sysConfig, request.method, request.requestURI))
    }

    @PutMapping("{id}")
    fun save(@PathVariable id: Long, @RequestBody sysConfig: SysConfig, request: HttpServletRequest): ResponseEntity<*> {
        val oldSysConfig = sysConfigService.findOne(id)
        JpaBeanUtil.copyNonNullProperties(sysConfig, oldSysConfig)
        return ResponseEntity.ok(sysConfigService.saveBySecurity(oldSysConfig, request.method, request.requestURI))
    }


    @DeleteMapping("delete")
    fun delete(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        sysConfigService.deleteBySecurity(id, request.method, request.requestURI)
        return ResponseEntity.noContent().build<Any>()
    }

}