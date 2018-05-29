package com.github.leon.aci.web.api

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.extenstions.pseudoPagination
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.generator.entity.Task
import org.joor.Reflect
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.util.ClassUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/metadata")
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
        val allContent = ApplicationProperties.entityScanPackages.map { it.replace(".", "/") + "/*.class" }
                .flatMap {
                    findClasses(BaseEntity::class.java, it)
                }.map { mutableMapOf("name" to it.name) }
        return allContent.pseudoPagination(pageable).responseEntityOk()

    }


    @GetMapping("/task")
    fun task(pageable: Pageable): ResponseEntity<Page<Task>> {
        //TaskConstants.init()
        val fqn = "com.rapiddev.generator.TaskConstants"
        val clz: Class<*> = Class.forName(fqn)
        val instance = clz.kotlin.objectInstance
        Reflect.on(instance).call("init")
        val list = findClasses(Task::class.java, "classpath*:com/rapiddev/generator/task/*/*.class")
                .map {
                    (it.newInstance() as Task)
                }
        return list.pseudoPagination(pageable).responseEntityOk()

    }

}

