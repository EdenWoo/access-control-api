package com.github.leon.aci.web.api

import com.github.leon.aci.domain.Permission
import com.github.leon.aci.service.PermissionService
import com.github.leon.bean.JpaBeanUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(value = ["/v1/permission"])

class PermissionController(
        @Autowired
        val permissionService: PermissionService

) {


    @GetMapping
    fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<*> {
        val page = permissionService.findBySecurity(request.method, request.requestURI, request.parameterMap, pageable)
        return ResponseEntity.ok(page)
    }

    @GetMapping("{id}")
    operator fun get(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<Permission> {
        return ResponseEntity.ok(permissionService.findOneBySecurity(id, request.method, request.requestURI))
    }

    @PostMapping
    fun save(@RequestBody permission: Permission, request: HttpServletRequest): ResponseEntity<Permission> {
        return ResponseEntity.ok(permissionService.saveBySecurity(permission, request.method, request.requestURI))
    }

    @PutMapping("{id}")
    fun save(@PathVariable id: Long, @RequestBody permission: Permission, request: HttpServletRequest): ResponseEntity<*> {
        val oldPermission = permissionService.findOne(id)
        JpaBeanUtil.copyNonNullProperties(permission, oldPermission)
        return ResponseEntity.ok(permissionService.saveBySecurity(oldPermission, request.method, request.requestURI))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        permissionService.deleteBySecurity(id, request.method, request.requestURI)
        return ResponseEntity.noContent().build<Any>()
    }
}