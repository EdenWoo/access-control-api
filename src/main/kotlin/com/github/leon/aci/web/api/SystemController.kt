package com.github.leon.aci.web.api

import arrow.core.None
import arrow.core.Some
import arrow.core.toOption
import com.github.leon.aci.dao.PermissionDao
import com.github.leon.aci.dao.RoleDao
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.domain.Permission
import com.github.leon.aci.domain.Role
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.aci.service.GeneratorService
import com.github.leon.aci.service.PermissionService
import com.github.leon.aci.service.RoleService
import com.github.leon.classpath.ClassSearcher
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ClassUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/metadata")
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

    fun findClasses(target: Class<*>, pattern: String): List<Class<*>> {
        val resourcePatternResolver = PathMatchingResourcePatternResolver()
        val metadataReaderFactory = CachingMetadataReaderFactory(resourcePatternResolver)
        val resources = resourcePatternResolver.getResources(pattern)
        return resources.map {
            ClassUtils.forName(metadataReaderFactory.getMetadataReader(it).classMetadata.className, Thread.currentThread().contextClassLoader)
        }.filter {
            target.isAssignableFrom(it) && it != target
        }

    }

    @GetMapping("/entity")
    fun entity(): ResponseEntity<List<String>> {
        val list = ApplicationProperties.entityScanPackages.map { it.replace(".","/")+"/*.class" }
                .flatMap {
                    findClasses(BaseEntity::class.java, it)
                }.map { it.name }
        return ResponseEntity.ok(list)

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
        val roleOpt = roleDao.findByName(roleName).toOption()
        val role = when (roleOpt) {
            is Some -> roleOpt.t
            None -> throw  IllegalArgumentException(roleName)
        }
        role.rolePermissions.clear()
        role.rolePermissions.addAll(userService.assignPermission(permissions, rule))
        roleService.save(role)
        //TODO
        //SharedConfig.CLEAN_ROLE.end();
        return ResponseEntity.ok(role)
    }
}
