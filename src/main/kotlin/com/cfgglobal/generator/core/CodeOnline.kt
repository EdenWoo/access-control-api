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
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull

fun String.remainLastIndexOf(string: String): String {
    return this.substring(this.lastIndexOf(string).inc())
}

fun main(args: Array<String>) {
    val templatePath = File(PathKit.getRootClassPath()).parent + "/resources/templates"
    val codeProject = CodeProject(
            utilClasses = listOf(Utils::class.java),
            packageName = "com.cfgglobal.Test",
            templatePath = templatePath,
//          targetPath = PathKit.getRootClassPath() + "/target",
            targetPath = "/Users/knight/CFG/smart-admin/src/app",
            scriptHelper = DefaultScriptHelper("groovy"),
            templateEngine = FreeMarkerHelper(templatePath)
    )

    val entities: MutableList<Class<out BaseEntity>> = ClassSearcher.of(BaseEntity::class.java).search()
    val codeEntities = entities.map { entity: Class<out BaseEntity> ->
        val codeEntity = CodeEntity(
                name = entity.simpleName
        )
        val fields = entity.declaredFields
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
                                println(it)
                                when (it) {
                                    is Column -> {
                                        codeField.unique = it.unique
                                        codeField.length = it.length
                                        codeField.scale = it.scale
                                        codeField.required = !it.nullable
                                    }
                                    is FieldFeature -> {
                                        codeField.searchable = it.searchable
                                        codeField.sortable = it.sortable
                                        codeField.display = it.display
                                    }
                                    is Id -> codeField.primaryKey = true
                                    is NotNull -> {
                                        codeField.required = true
                                    }


                                    else -> {

                                    }
                                }
                            }
                    codeField
                }
        codeEntity.fields = fields
        codeEntity
    }

    codeProject.entities = codeEntities

    val tasks = mutableListOf<Task>()

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

    //app.component.ts
    val appComponentTs = Task(
            name = "app.component.ts",
            folder = """"/"""",
            taskType = "single",
            filename = """"app.component.ts"""",
            templatePath = """"angular/app.component.ts""""
    )
    tasks.add(appComponentTs)

    //shared-module.ts
    val sharedModuleTsService = Task(
            name = "shared-module.component.ts",
            folder = """ "shared-module"""",
            taskType = "single",
            filename = """"shared.module.ts"""",
            templatePath = """"angular/shared-module/shared.module.ts""""
    )
    tasks.add(sharedModuleTsService)

    //multi-select.html
    val multiSelectHtmlService = Task(
            name = "multi-select.component.html",
            folder = """ "shared-module/multi-select/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-multi-select.component.html"""",
            templatePath = """"angular/shared-module/multi-select/multi-select.component.html""""
    )
    tasks.add(multiSelectHtmlService)


    //multi-select.html
    val multiSelectTsService = Task(
            name = "multi-select.component.ts",
            folder = """ "shared-module/multi-select/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-multi-select.component.ts"""",
            templatePath = """"angular/shared-module/multi-select/multi-select.component.ts""""
    )
    tasks.add(multiSelectTsService)

    //-------Store-------
    //action.constant
    val actionConstantTsService = Task(
            name = "action.constant.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"action.constant.ts"""",
            templatePath = """"angular/store/actions/action.constant.ts""""
    )
    tasks.add(actionConstantTsService)

    //actions
    val actionsTsService = Task(
            name = "actions.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"actions.ts"""",
            templatePath = """"angular/store/actions/actions.ts""""
    )
    tasks.add(actionsTsService)

    //add.action
    val addActionTsService = Task(
            name = "add.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"add.action.ts"""",
            templatePath = """"angular/store/actions/add.action.ts""""
    )
    tasks.add(addActionTsService)

    //delete.action
    val deleteActionTsService = Task(
            name = "add.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"delete.action.ts"""",
            templatePath = """"angular/store/actions/delete.action.ts""""
    )
    tasks.add(deleteActionTsService)

    //fetch.action
    val fetchActionTsService = Task(
            name = "add.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"fetch.action.ts"""",
            templatePath = """"angular/store/actions/fetch.action.ts""""
    )
    tasks.add(fetchActionTsService)

    //set.action
    val setActionTsService = Task(
            name = "set.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"set.action.ts"""",
            templatePath = """"angular/store/actions/set.action.ts""""
    )
    tasks.add(setActionTsService)

    //update.action
    val updateActionTsService = Task(
            name = "update.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"update.action.ts"""",
            templatePath = """"angular/store/actions/update.action.ts""""
    )
    tasks.add(updateActionTsService)

    //store.action
    val storeActionTsService = Task(
            name = "store.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"store.action.ts"""",
            templatePath = """"angular/store/actions/store.action.ts""""
    )
    tasks.add(storeActionTsService)

    //interfaces
    //feature-state.interface.ts
    val featureStateInterfaceTsService = Task(
            name = "feature-state.interface.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/interfaces" """,
            taskType = "multiple",
            filename = """"feature-state.interface.ts"""",
            templatePath = """"angular/store/interfaces/feature-state.interface.ts""""
    )
    tasks.add(featureStateInterfaceTsService)

    //interfaces
    //feature-state.interface.ts
    val stateInterfaceTsService = Task(
            name = "state.interface.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/interfaces" """,
            taskType = "multiple",
            filename = """"state.interface.ts"""",
            templatePath = """"angular/store/interfaces/state.interface.ts""""
    )
    tasks.add(stateInterfaceTsService)

    //effects.ts
    val effectsTsService = Task(
            name = "effects.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
            taskType = "multiple",
            filename = """"effects.ts"""",
            templatePath = """"angular/store/effects.ts""""
    )
    tasks.add(effectsTsService)

    //initial-state.constant.ts
    val initialStateConstantTsService = Task(
            name = "initial-state.constant.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
            taskType = "multiple",
            filename = """"initial-state.constant.ts"""",
            templatePath = """"angular/store/initial-state.constant.ts""""
    )
    tasks.add(initialStateConstantTsService)

    //reducers.ts
    val reducersTsService = Task(
            name = "reducers.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
            taskType = "multiple",
            filename = """"reducers.ts"""",
            templatePath = """"angular/store/reducers.ts""""
    )
    tasks.add(reducersTsService)

    //app-store
    //app-reducers.constant.ts
    val appReducersConstantTsService = Task(
            name = "app-reducers.constant.ts",
            folder = """"app-store/"""",
            taskType = "single",
            filename = """"app-reducers.constant.ts"""",
            templatePath = """"angular/store/app-store/app-reducers.constant.ts""""
    )
    tasks.add(appReducersConstantTsService)

    //app-reducers.constant.ts
    val appStateInterfaceTsService = Task(
            name = "app-state.interface.ts",
            folder = """"app-store/"""",
            taskType = "single",
            filename = """"app-state.interface.ts"""",
            templatePath = """"angular/store/app-store/app-state.interface.ts""""
    )
    tasks.add(appStateInterfaceTsService)

//navigation.component.html.ftl
    val navigationHtml = Task(
            name = "navigationHtml",
            folder = """"shared/layout/navigation"""",
            taskType = "single",
            filename = """"navigation.component.html"""",
            templatePath = """"angular/navigation.component.html.ftl""""
    )
    tasks.add(navigationHtml)

    for (t in tasks) {
        TaskService.processTask(codeProject, t)
    }
}



