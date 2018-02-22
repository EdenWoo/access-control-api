package com.cfgglobal.test.web.api

import com.cfgglobal.test.domain.RolePermission
import com.cfgglobal.test.service.PermissionService
import com.cfgglobal.test.service.rule.RuleService
import com.cfgglobal.test.web.base.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = ["/v1/role-permission"])

class RolePermissionController(
        @Autowired
        val ruleService: RuleService,

        @Autowired
        val permissionService: PermissionService

) : BaseController() {

    @GetMapping
    fun list(): ResponseEntity<List<RolePermission>> {
        val rolePermissions = permissionService.findAll()
                .map { permission -> RolePermission(permission = permission, rules = ruleService.findAll().toMutableList()) }
        return ResponseEntity.ok(rolePermissions)

    }

}