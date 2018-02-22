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
import com.cfgglobal.test.base.ClassSearcher
import com.cfgglobal.test.base.PathKit
import com.cfgglobal.test.domain.BaseEntity
import com.google.common.collect.Lists
import lombok.extern.slf4j.Slf4j

import javax.persistence.Column
import javax.persistence.Id
import java.io.File

@Slf4j
object CodeOnline {

@JvmStatic  fun main(args:Array<String>) {
 //CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert()
        val codeProject = CodeProject()
val classes = Lists.newArrayList<Class<*>>()
 //classes.add(TaskService)
        classes.add(Utils::class.java)
codeProject.utilClasses = classes

codeProject.packageName = "com.cfgglobal.test"

codeProject.templatePath = File(PathKit.getRootClassPath()).getParent() + "/resources/templates"

codeProject.targetPath = "/Users/knight/CFG/smart-admin/src/app"
 //        codeProject.setTargetPath(PathKit.getRootClassPath() + "/target");

        codeProject.scriptHelper = DefaultScriptHelper("groovy")

codeProject.templateEngine = FreeMarkerHelper(codeProject.templatePath)

val entities = ClassSearcher.of(BaseEntity::class.java)
 //.libDir("/Users/leon/IdeaProjects/collinson-backend/collinson-base/build/libs")
                //.includeAllJarsInLib(true)
                .search()
val codeEntities = io.vavr.collection.List.ofAll<Class<*>>(entities).map({ e->
val codeEntity = CodeEntity()
codeEntity.name = e.getSimpleName()
val fields = io.vavr.collection.List.of<Field>(*e.getDeclaredFields())
.map{ f ->
            val codeField = CodeField()
            codeField.name = f.getName()
            val type = f.getType()
            if (List<*>::class.java.isAssignableFrom(type)) {
                codeField.type = "List"
            } else if (BaseEntity::class.java.isAssignableFrom(type)) {
                codeField.type = "Entity"
            } else {
                codeField.type = type.getSimpleName()
            }
            val annotations = f.getDeclaredAnnotations()
            io.vavr.collection.List.of<Annotation>(*annotations)
                    .forEach { annotation ->
                        if (annotation is Column) {
                            codeField.isUnique = annotation.unique()
                            codeField.length = annotation.length()
                            codeField.isRequired = !annotation.nullable()
                            codeField.scale = annotation.scale()
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
codeEntity }).asJavaMutable()

codeProject.entities = codeEntities
val tasks = Lists.newArrayList<Task>()

val daoTask = Task()
daoTask.name = "DAO"
daoTask.folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"dao\""
daoTask.taskType = "multiple"
daoTask.filename = "entity.name+\"Dao.java\""
daoTask.templatePath = "\"java/dao.ftl\""
tasks.add(daoTask)

val serviceTask = Task()
serviceTask.name = "SERVICE"
serviceTask.folder = "\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"service\""
serviceTask.taskType = "multiple"
serviceTask.filename = "entity.name+\"Service.java\""
serviceTask.templatePath = "\"java/service.ftl\""
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
        val entityFormComponentTs = Task()
entityFormComponentTs.name = "entity-form.component.ts"
entityFormComponentTs.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form\""
entityFormComponentTs.taskType = "multiple"
entityFormComponentTs.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form.component.ts\""
entityFormComponentTs.templatePath = "\"angular/entity-form/entity-form_component_ts.ftl\""
tasks.add(entityFormComponentTs)

 // entity-form.component.html
        val entityFormComponentHtml = Task()
entityFormComponentHtml.name = "entity-form.component.html"
entityFormComponentHtml.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form\""
entityFormComponentHtml.taskType = "multiple"
entityFormComponentHtml.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-form.component.html\""
entityFormComponentHtml.templatePath = "\"angular/entity-form/entity-form_component_html.ftl\""
tasks.add(entityFormComponentHtml)


 // entity-list.component.ts
        val entityListComponentTs = Task()
entityListComponentTs.name = "entity-list.component.ts"
entityListComponentTs.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list\""
entityListComponentTs.taskType = "multiple"
entityListComponentTs.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list.component.ts\""
entityListComponentTs.templatePath = "\"angular/entity-list/entity-list_component_ts.ftl\""
tasks.add(entityListComponentTs)

 // entity-list.component.html
        val entityListComponentHtml = Task()
entityListComponentHtml.name = "entity-list.component.html"
entityListComponentHtml.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list\""
entityListComponentHtml.taskType = "multiple"
entityListComponentHtml.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list.component.html\""
entityListComponentHtml.templatePath = "\"angular/entity-list/entity-list_component_html.ftl\""
tasks.add(entityListComponentHtml)

 // sort.columns.ts
        val SortColums = Task()
SortColums.name = "sort.columns.ts"
SortColums.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\"-list\""
SortColums.taskType = "multiple"
SortColums.filename = "\"sort.columns.ts\""
SortColums.templatePath = "\"angular/entity-list/sort_columns_ts.ftl\""
tasks.add(SortColums)

 //service.html
        val entityService = Task()
entityService.name = "entity-service.component.html"
entityService.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)"
entityService.taskType = "multiple"
entityService.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\".service.ts\" "
entityService.templatePath = "\"angular/entity_service_ts.ftl\""
tasks.add(entityService)

 //module.html
        val entityModule = Task()
entityModule.name = "entity-module.component.html"
entityModule.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)"
entityModule.taskType = "multiple"
entityModule.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\".module.ts\" "
entityModule.templatePath = "\"angular/entity_module_ts.ftl\""
tasks.add(entityModule)

 //model.html
        val entityModel = Task()
entityModel.name = "entity-model.component.html"
entityModel.folder = "\"pages/\"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)"
entityModel.taskType = "multiple"
entityModel.filename = "com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+\".model.ts\""
entityModel.templatePath = "\"angular/entity_model_ts.ftl\""
tasks.add(entityModel)

 //pages_routing.ts
        val pageRouting = Task()
pageRouting.name = "pagesoutings"
pageRouting.folder = "\"pages/\""
pageRouting.taskType = "multiple"
pageRouting.filename = "\"pages.routing.ts\""
pageRouting.templatePath = "\"angular/pages_routing_ts.ftl\""
tasks.add(pageRouting)

 //app.module.ts
        val appModule = Task()
appModule.name = "appModule"
appModule.folder = "\"/\""
appModule.taskType = "single"
appModule.filename = "\"app.module.ts\""
appModule.templatePath = "\"angular/app_module_ts.ftl\""
tasks.add(appModule)

for (t in tasks)
{
TaskService.processTask(codeProject, t)
}
}


}
