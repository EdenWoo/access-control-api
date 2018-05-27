package com.github.leon.aci.web.api

import com.github.leon.aci.dao.PermissionDao
import com.github.leon.aci.dao.RoleDao
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.aci.service.GeneratorService
import com.github.leon.aci.service.PermissionService
import com.github.leon.aci.service.RoleService
import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.http.ResponseEntity
import org.springframework.util.ClassUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/metadata")
class MetadataController(
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
    fun entity(): ResponseEntity<List<Map<String, Any?>>> {
        val list = ApplicationProperties.entityScanPackages.map { it.replace(".", "/") + "/*.class" }
                .flatMap {
                    findClasses(BaseEntity::class.java, it)
                }.map { mutableMapOf("name" to it.name) }
        return ResponseEntity.ok(list)

    }

    @GetMapping("/task")
    fun task(): ResponseEntity<List<Task>> {
        Reflect.on("com.rapiddev.generator.TaskConstants").call("init")
        val list = findClasses(Task::class.java, "classpath*:com/rapiddev/generator/task/*/*.class")
                .map {
                    (it.newInstance() as Task)
                }
        return list.responseEntityOk()

    }

}
