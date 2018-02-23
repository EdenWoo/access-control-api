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
import lombok.extern.slf4j.Slf4j
import java.io.File
import javax.persistence.Column
import javax.persistence.Id

@Slf4j
object CodeOnline {

    @JvmStatic
    fun main(args: Array<String>) {

        val classes = Lists.newArrayList<Class<*>>()
        classes.add(Utils::class.java)
        val templatePath = File(PathKit.getRootClassPath()).parent + "/resources/templates"
        val codeProject = CodeProject(
                utilClasses = classes,
                packageName = "com.cfgglobal.test",
                templatePath = templatePath,
                targetPath = "/Users/knight/CFG/smart-admin/src/app",
                //        codeProject.setTargetPath(PathKit.getRootClassPath() + "/target");
                scriptHelper = DefaultScriptHelper("groovy"),
                templateEngine = FreeMarkerHelper(templatePath)
        )

        val entities: MutableList<Class<out BaseEntity>> = ClassSearcher.of(BaseEntity::class.java)
                //.libDir("/Users/leon/IdeaProjects/collinson-backend/collinson-base/build/libs")
                //.includeAllJarsInLib(true)
                .search()
        val codeEntities = entities.map({ e ->
            val codeEntity = CodeEntity()
            codeEntity.name = e.getSimpleName()
            val fields = e.getDeclaredFields()
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
                    }
            codeEntity.fields = fields.toMutableList()
            codeEntity
        })

        codeProject.entities = codeEntities.toMutableList()
        val tasks = Lists.newArrayList<Task>()

        val daoTask = Task(
                name = "DAO",
                folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"dao\"",
                taskType = "multiple",
                filename = "entity.name+\"Dao.java\"",
                templatePath = "\"java/dao.ftl\""
        )
        tasks.add(daoTask)

        val serviceTask = Task(
                name = "SERVICE",
                folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"service\"",
                taskType = "multiple",
                filename = "entity.name+\"Service.java\"",
                templatePath = "\"java/service.ftl\""
        )
        tasks.add(serviceTask)

        //        Task testTask = new Task();
        //        testTask.setName("TEST");
        //        testTask.setFolder("\"angular\"");
        //        testTask.setTaskType("multiple");
        //        testTask.setFilename("entity.name+\"Test.ts\"");
        //        testTask.setTemplatePath("\"angular/test.ftl\"");
        //        tasks.add(testTask);


        //=======================================
        //==============Angular==================

        // entity-form.component.ts
        val entityFormComponentTs = Task(
                name = "entity-form.component.ts",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form\"",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form.component.ts\"",
                templatePath = "\"angular/entity-form/entity-form_component_ts.ftl\""
        )
        tasks.add(entityFormComponentTs)

        // entity-form.component.html
        val entityFormComponentHtml = Task(
                name = "entity-form.component.html",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form\"",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form.component.html\"",
                templatePath = "\"angular/entity-form/entity-form_component_html.ftl\""
        )
        tasks.add(entityFormComponentHtml)


        // entity-list.component.ts
        val entityListComponentTs = Task(
                name = "entity-list.component.ts",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list\"",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list.component.ts\"",
                templatePath = "\"angular/entity-list/entity-list_component_ts.ftl\""
        )
        tasks.add(entityListComponentTs)

        // entity-list.component.html
        val entityListComponentHtml = Task(
                name = "entity-list.component.html",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list\"",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list.component.html\"",
                templatePath = "\"angular/entity-list/entity-list_component_html.ftl\""
        )
        tasks.add(entityListComponentHtml)

        // sort.columns.ts
        val SortColums = Task(
                name = "sort.columns.ts",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list\"",
                taskType = "multiple",
                filename = "\"sort.columns.ts\"",
                templatePath = "\"angular/entity-list/sort_columns_ts.ftl\""
        )
        tasks.add(SortColums)

        //service.html
        val entityService = Task(
                name = "entity-service.component.html",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\".service.ts\" ",
                templatePath = "\"angular/entity_service_ts.ftl\""
        )
        tasks.add(entityService)

        //module.html
        val entityModule = Task(
                name = "entity-module.component.html",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\".module.ts\" ",
                templatePath = "\"angular/entity_module_ts.ftl\""
        )
        tasks.add(entityModule)

        //model.html
        val entityModel = Task(
                name = "entity-model.component.html",
                folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)",
                taskType = "multiple",
                filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\".model.ts\"",
                templatePath = "\"angular/entity_model_ts.ftl\""
        )
        tasks.add(entityModel)

        //pages_routing.ts
        val pageRouting = Task(
                name = "pagesoutings",
                folder = "\"pages/\"",
                taskType = "multiple",
                filename = "\"pages.routing.ts\"",
                templatePath = "\"angular/pages_routing_ts.ftl\""
        )
        tasks.add(pageRouting)

        //app.module.ts
        val appModule = Task(
                name = "appModule",
                folder = "\"/\"",
                taskType = "single",
                filename = "\"app.module.ts\"",
                templatePath = "\"angular/app_module_ts.ftl\""
        )
        tasks.add(appModule)

        for (t in tasks) {
            TaskService.processTask(codeProject, t)
        }
    }


}
