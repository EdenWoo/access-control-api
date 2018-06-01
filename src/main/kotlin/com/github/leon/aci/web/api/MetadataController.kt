package com.github.leon.aci.web.api

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.extenstions.pseudoPagination
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.generator.entity.CodeEnv
import com.github.leon.generator.entity.Task
import org.joor.Reflect
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.util.ClassUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class MetadataController {

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
    fun entity(pageable: Pageable): ResponseEntity<Page<MutableMap<String, String>>> {
        val allContent = allEntities()

        return allContent.pseudoPagination(pageable).responseEntityOk()

    }


    @GetMapping("/task")
    fun task(pageable: Pageable): ResponseEntity<Page<Task>> {
        val list = allTaskes()
        return list.pseudoPagination(pageable).responseEntityOk()

    }

    @PostMapping("/generate")
    fun generate(env: CodeEnv): ResponseEntity<String> {
        val selectedEntity = allEntities().filter { env.entities.any { e -> e.name == it["name"] } }
        println(selectedEntity)
        val selectedTask = allTaskes().filter { env.taskes.any { e -> e.name == it.name } }
        println(selectedEntity)
        return "entity ${selectedEntity.size} task ${selectedTask.size}".responseEntityOk()

    }

    private fun allTaskes(): List<Task> {
        val clz: Class<*> = Class.forName("com.github.leon.template.TaskConstants")
        val instance = clz.kotlin.objectInstance
        Reflect.on(instance).call("init")
        return findClasses(Task::class.java, "classpath*:com/github/leon/template/task/*/*.class")
                .map {
                    (it.newInstance() as Task)
                }
    }

    private fun allEntities(): List<MutableMap<String, String>> {
        return ApplicationProperties.entityScanPackages.map { it.replace(".", "/") + "/*.class" }
                .flatMap {
                    findClasses(BaseEntity::class.java, it)
                }.map { mutableMapOf("name" to it.name) }
    }

}

