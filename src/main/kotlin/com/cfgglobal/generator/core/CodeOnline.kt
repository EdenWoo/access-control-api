package com.cfgglobal.generator.core


import com.cfgglobal.generator.entity.*
import com.cfgglobal.generator.ext.Utils
import com.cfgglobal.generator.metadata.FieldFeature
import com.cfgglobal.generator.script.DefaultScriptHelper
import com.cfgglobal.generator.task.service.TaskService
import com.cfgglobal.generator.template.FreeMarkerHelper
import com.cfgglobal.test.domain.BaseEntity
import com.github.leon.classpath.ClassSearcher
import com.github.leon.classpath.PathKit
import java.io.File
import java.lang.reflect.ParameterizedType
import javax.persistence.Column
import javax.persistence.Id

fun String.remainLastIndexOf(string: String): String {
    return this.substring(this.lastIndexOf(string).inc())
}

fun main(args: Array<String>) {
    val templatePath = File(PathKit.getRootClassPath()).parent + "/resources/templates"
    val codeProject = CodeProject(
            utilClasses = listOf(Utils::class.java),
            packageName = "com.cfgglobal.test",
            templatePath = templatePath,
//            targetPath = PathKit.getRootClassPath() + "/target",
            targetPath = "/Users/knight/CFG/smart-admin/src/app",
            scriptHelper = DefaultScriptHelper("groovy"),
            templateEngine = FreeMarkerHelper(templatePath)
    )

    val entities: MutableList<Class<out BaseEntity>> = ClassSearcher.of(BaseEntity::class.java).search()
    val codeEntities = entities.map {
        val codeEntity = CodeEntity(
                name = it.simpleName
        )
        val fields = it.declaredFields
                .map {
                    val codeField = CodeField(
                            name = it.name,
                            type = when {
                                List::class.java.isAssignableFrom(it.type) -> FieldType(name = "List", element = (it.genericType as ParameterizedType).actualTypeArguments[0].typeName.remainLastIndexOf("."))
                                BaseEntity::class.java.isAssignableFrom(it.type) -> FieldType(name = "Entity", element = it.type.simpleName)
                                else -> FieldType(name = it.type.simpleName)
                            }
                    )
                    it.declaredAnnotations
                            .forEach {
                                when (it) {
                                    is Column -> {
                                        codeField.isUnique = it.unique
                                        codeField.length = it.length
                                        codeField.required = !it.nullable
                                        codeField.scale = it.scale
                                    }
                                    is FieldFeature -> {
                                        codeField.searchable = it.searchable
                                        codeField.sortable = it.sortable
                                    }
                                    is Id -> codeField.isPrimaryKey = true
                                    else -> {

                                    }
                                }
                            }
                    codeField
                }
        codeEntity.fields = fields
        println(fields)
        codeEntity
    }

    codeProject.entities = codeEntities

    val tasks  = mutableListOf<Task>()

    val daoTask = Task(
            name = "DAO",
            folder = """"src/main/java/"+project.packageName.replaceAll("\\.","/")+"/"+"dao"""",
            taskType = "multiple",
            filename = """entity.name+"Dao.java"""",
            templatePath = """"java/dao.ftl""""
    )
    tasks.add(daoTask)

    val serviceTask = Task(
            name = "SERVICE",
            folder = """"src/main/java/"+project.packageName.replaceAll("\\.","/")+"/"+"service"""",
            taskType = "multiple",
            filename = """entity.name+"Service.java"""",
            templatePath = """"java/service.ftl""""
    )
    tasks.add(serviceTask)

    //=======================================
    //==============Angular==================

    // entity-form.component.ts
    val entityFormComponentTs = Task(
            name = "entity-form.component.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form.component.ts"""",
            templatePath = """"angular/entity-form/entity-form_component_ts.ftl""""
    )
    tasks.add(entityFormComponentTs)

    // entity-form.component.html
    val entityFormComponentHtml = Task(
            name = "entity-form.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form.component.html"""",
            templatePath = """"angular/entity-form/entity-form_component_html.ftl""""
    )
    tasks.add(entityFormComponentHtml)


    // entity-list.component.ts
    val entityListComponentTs = Task(
            name = "entity-list.component.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list.component.ts"""",
            templatePath = """"angular/entity-list/entity-list_component_ts.ftl""""
    )
    tasks.add(entityListComponentTs)

    // entity-list.component.html
    val entityListComponentHtml = Task(
            name = "entity-list.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list.component.html"""",
            templatePath = """"angular/entity-list/entity-list_component_html.ftl""""
    )
    tasks.add(entityListComponentHtml)

    // sort.columns.ts
    val SortColums = Task(
            name = "sort.columns.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
            taskType = "multiple",
            filename = """"sort.columns.ts"""",
            templatePath = """"angular/entity-list/sort_columns_ts.ftl""""
    )
    tasks.add(SortColums)

    //service.html
    val entityService = Task(
            name = "entity-service.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """ com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+".service.ts" """,
            templatePath = """"angular/entity_service_ts.ftl""""
    )
    tasks.add(entityService)

    //module.html
    val entityModule = Task(
            name = "entity-module.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+".module.ts" """,
            templatePath = """"angular/entity_module_ts.ftl""""
    )
    tasks.add(entityModule)

    //model.html
    val entityModel = Task(
            name = "entity-model.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+".model.ts"""",
            templatePath = """"angular/entity_model_ts.ftl""""
    )
    tasks.add(entityModel)

    //pages_routing.ts
    val pageRouting = Task(
            name = "pagesoutings",
            folder = """"pages/"""",
            taskType = "multiple",
            filename = """"pages.routing.ts"""",
            templatePath = """"angular/pages_routing_ts.ftl""""
    )
    tasks.add(pageRouting)

    //app.module.ts
    val appModule = Task(
            name = "appModule",
            folder = """"/"""",
            taskType = "single",
            filename = """"app.module.ts"""",
            templatePath = """"angular/app_module_ts.ftl""""
    )
    tasks.add(appModule)

    for (t in tasks) {
        TaskService.processTask(codeProject, t)
    }
}



