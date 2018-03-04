package com.cfgglobal.test.web.api

import com.cfgglobal.test.dao.PermissionDao
import com.cfgglobal.test.dao.RoleDao
import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.domain.Role
import com.cfgglobal.test.service.GeneratorService
import com.cfgglobal.test.service.PermissionService
import com.cfgglobal.test.service.RoleService
import com.github.leon.classpath.ClassSearcher
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sys")
class SystemController(
        @Autowired
        val userService: GeneratorService,
        @Autowired
        val permissionService: PermissionService,
        @Autowired
        val roleService: RoleService,
        @Autowired
        val permissionDao: PermissionDao,
        @Autowired
        val roleDao: RoleDao

) {


    @GetMapping("/entity")
    fun entity(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(ClassSearcher.of(BaseEntity::class.java).search<BaseEntity>().map { e -> e.simpleName })

    }

    @RequestMapping(value = ["/permission"], method = [(RequestMethod.GET), (RequestMethod.POST)])
    @Transactional
    fun init(entityName: String?): ResponseEntity<List<Permission>> {
        val permissions = mutableListOf<Permission>()
        val entityToPermission = { entity: BaseEntity ->
            val name = entity.javaClass.simpleName

            permissionDao.delete(permissionDao.findByEntity(name))
            permissions.addAll(userService.genPermission(entity))
            permissionService.save(permissions)

        }
        if (entityName != null) {
            val name = BaseEntity::class.java.`package`.name + "." + entityName
            println("entity:  " + name)
            val reflect = Reflect.on(name)
            entityToPermission(reflect.create().get<BaseEntity>())
        } else {
            ClassSearcher.of(BaseEntity::class.java).search<BaseEntity>()
                    .map { e ->
                        val reflect = Reflect.on(e.name)
                        reflect.create().get<BaseEntity>()
                    }
                    .forEach { entityToPermission }

        }
        return ResponseEntity.ok(permissions.toList())
    }

    @GetMapping("/assign")
    @Transactional
    fun assign(roleName: String, rule: String): ResponseEntity<Role> {
        val permissions = permissionService.findAll()
        val role = roleDao.findByName(roleName).getOrElseThrow { IllegalArgumentException(roleName) }
        role.rolePermissions.clear()
        role.rolePermissions.addAll(userService.assignPermission(permissions, rule))
        roleService.save(role)
        //TODO
        //SharedConfig.CLEAN_ROLE.end();
        return ResponseEntity.ok(role)
    }
}
