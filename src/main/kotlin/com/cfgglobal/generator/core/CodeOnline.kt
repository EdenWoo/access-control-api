package com.cfgglobal.generator.core


import com.cfgglobal.generator.entity.CodeEntity
import com.cfgglobal.generator.entity.CodeField
import com.cfgglobal.generator.entity.CodeProject
import com.cfgglobal.generator.entity.Task
import com.cfgglobal.generator.ext.Utils
import com.cfgglobal.generator.metadata.FieldFeature
import com.cfgglobal.generator.script.DefaultScriptHelper
import com.cfgglobal.generator.task.service.TaskService
import com.cfgglobal.generator.template.FreeMarkerHelper

import com.cfgglobal.test.domain.BaseEntity
import com.github.leon.classpath.ClassSearcher
import com.github.leon.classpath.PathKit
import com.google.common.collect.Lists
import java.io.File
import java.lang.reflect.Field
import javax.persistence.Column
import javax.persistence.Id


object CodeOnline {

    @JvmStatic
    fun main(args: Array<String>) {
        //CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert()
        val classes = Lists.newArrayList<Class<*>>()
        classes.add(Utils::class.java)
        val templatePath = File(PathKit.getRootClassPath()).getParent() + "/resources/templates"
        val codeProject = CodeProject(
                utilClasses = classes,
                packageName = "com.cfgglobal.ccfx",
                templatePath = templatePath,
                targetPath = PathKit.getRootClassPath() + "/target",
                scriptHelper = DefaultScriptHelper("groovy"),
                templateEngine = FreeMarkerHelper(templatePath)
        )

        var entities = ClassSearcher.of(BaseEntity::class.java).search<BaseEntity>()

        //.libDir("/Users/leon/IdeaProjects/collinson-backend/collinson-base/build/libs")
        //.includeAllJarsInLib(true)

        val codeEntities = io.vavr.collection.List.ofAll<Class<*>>(entities).map({ e ->
            val codeEntity = CodeEntity()
            codeEntity.name = e.getSimpleName()
            val fields = io.vavr.collection.List.of<Field>(*e.getDeclaredFields())
                    .map { f ->
                        val codeField = CodeField()
                        codeField.name = f.getName()
                        val type = f.getType()
                        when {
                            List::class.java.isAssignableFrom(type) -> codeField.type = "List"
                            BaseEntity::class.java.isAssignableFrom(type) -> codeField.type = "Entity"
                            else -> codeField.type = type.getSimpleName()
                        }
                        val annotations = f.getDeclaredAnnotations()
                        io.vavr.collection.List.of<Annotation>(*annotations)
                                .forEach { annotation ->
                                    if (annotation is Column) {
                                        codeField.isUnique = annotation.unique
                                        codeField.length = annotation.length
                                        codeField.isRequired = !annotation.nullable
                                        codeField.scale = annotation.scale
                                    } else if (annotation is FieldFeature) {
                                        codeField.isSearchable = annotation.searchable
                                        codeField.isSortable = annotation.sortable
                                    } else if (annotation is Id) {
                                        codeField.isPrimaryKey = true
                                    } else {

                                    }
                                }
                        codeField
                    }.toJavaList()
            codeEntity.fields = fields
            codeEntity
        }).asJavaMutable()

        codeProject.entities = codeEntities
        val tasks = Lists.newArrayList<Task>()

        val daoTask = Task(
                name = "DAO",
                folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"dao\"",
                taskType = "multiple",
                filename = "entity.name+\"Dao.java\"",
                templatePath = "\"java/dao.ftl\"")
        tasks.add(daoTask)

        val serviceTask = Task(
                name = "SERVICE",
                folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"service\"",
                taskType = "multiple",
                filename = "entity.name+\"Service.java\"",
                templatePath = "\"java/service.ftl\""
        )
        tasks.add(serviceTask)

        val controllerTask = Task(
                name = "CONTROLLER",
                folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"controller\"",
                taskType = "multiple",
                filename = "entity.name+\"Controller.java\"",
                templatePath = "\"java/controller.ftl\""
        )
        tasks.add(controllerTask)


        val testTask = Task(
                name = "TEST",
                folder = "\"angular\"",
                taskType = "multiple",
                filename = "entity.name+\"Test.ts\"",
                templatePath = "\"angular/ccfx.ftl\"")
        tasks.add(testTask)

        for (t in tasks) {
            TaskService.processTask(codeProject, t)
        }
    }


}
