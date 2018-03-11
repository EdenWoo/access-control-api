package com.cfgglobal.generator.entity


import com.cfgglobal.generator.task.processor.ITaskProcessor
import org.apache.commons.lang3.StringUtils
import org.joor.Reflect


data class Task(
        var id: Int? = null,

        var name: String,

        var taskType: String,

        var taskOfProject:TaskOfProject,

        var folder: String,

        var filename: String,

        var templatePath: String
) {

    fun run(codeProject: CodeProject, root: MutableMap<String, Any>): List<String> {
        val paths = taskProcessor(this.taskType).run(codeProject, this, root)
        return paths
    }

    private fun taskProcessor(taskType: String): ITaskProcessor {
        return Reflect.on(ITaskProcessor::class.java.`package`.name + "."
                + StringUtils.capitalize(taskType) +
                "TaskProcessor").create().get()
    }


}

fun apiTasks(): List<Task> {

    val daoTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "DAO",
            folder = """ project.packageName.replaceAll("\\.","/")+"/"+"dao" """,
            taskType = "multiple",
            filename = """ entity.name+"Dao.kt" """,
            templatePath = """ "kotlin/dao.ftl" """
    )

    val serviceTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "SERVICE",
            folder = """ project.packageName.replaceAll("\\.","/")+"/"+"service" """,
            taskType = "multiple",
            filename = """ entity.name+"Service.kt" """,
            templatePath = """ "kotlin/service.ftl" """
    )

    val controllerTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "SERVICE",
            folder = """ project.packageName.replaceAll("\\.","/")+"/"+"controller" """,
            taskType = "multiple",
            filename = """ entity.name+"Controller.kt" """,
            templatePath = """ "kotlin/controller.ftl" """
    )
    return listOf(daoTask, serviceTask, controllerTask)
}

fun uiTasks(): List<Task> {

    //=======================================
    //==============Angular==================

    // entity-form.component.ts
    val entityFormComponentTs = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-form.component.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form.component.ts"""",
            templatePath = """"angular/entity-form/entity-form_component_ts.ftl""""
    )

    // entity-form.component.html
    val entityFormComponentHtml = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-form.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-form.component.html"""",
            templatePath = """"angular/entity-form/entity-form_component_html.ftl""""
    )


    // entity-list.component.ts
    val entityListComponentTs = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-list.component.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list.component.ts"""",
            templatePath = """"angular/entity-list/entity-list_component_ts.ftl""""
    )

    // entity-list.component.html
    val entityListComponentHtml = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-list.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list.component.html"""",
            templatePath = """"angular/entity-list/entity-list_component_html.ftl""""
    )

    // sort.columns.ts
    val SortColums = Task(
            taskOfProject = TaskOfProject.UI,
            name = "sort.columns.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
            taskType = "multiple",
            filename = """"sort.columns.ts"""",
            templatePath = """"angular/entity-list/sort_columns_ts.ftl""""
    )

    //service.html
    val entityService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-service.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """ com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+".service.ts" """,
            templatePath = """"angular/entity_service_ts.ftl""""
    )

    //module.html
    val entityModule = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-module.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+".module.ts" """,
            templatePath = """"angular/entity_module_ts.ftl""""
    )

    //model.html
    val entityModel = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-model.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+".model.ts"""",
            templatePath = """"angular/entity_model_ts.ftl""""
    )

    //pages_routing.ts
    val pageRouting = Task(
            taskOfProject = TaskOfProject.UI,
            name = "pagesoutings",
            folder = """"pages/"""",
            taskType = "multiple",
            filename = """"pages.routing.ts"""",
            templatePath = """"angular/pages_routing_ts.ftl""""
    )

    //app.module.ts
    val appModule = Task(
            taskOfProject = TaskOfProject.UI,
            name = "appModule",
            folder = """"/"""",
            taskType = "single",
            filename = """"app.module.ts"""",
            templatePath = """"angular/app_module_ts.ftl""""
    )

    //app.component.ts
    val appComponentTs = Task(
            taskOfProject = TaskOfProject.UI,
            name = "app.component.ts",
            folder = """"/"""",
            taskType = "single",
            filename = """"app.component.ts"""",
            templatePath = """"angular/app.component.ts""""
    )

    //shared-module.ts
    val sharedModuleTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "shared-module.component.ts",
            folder = """ "shared-module"""",
            taskType = "single",
            filename = """"shared.module.ts"""",
            templatePath = """"angular/shared-module/shared.module.ts""""
    )


    //multi-select.html
    val multiSelectHtmlService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "multi-select.component.html",
            folder = """ "shared-module/multi-select/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-multi-select.component.html"""",
            templatePath = """"angular/shared-module/multi-select/multi-select.component.html""""
    )


    //multi-select.html
    val multiSelectTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "multi-select.component.ts",
            folder = """ "shared-module/multi-select/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-multi-select.component.ts"""",
            templatePath = """"angular/shared-module/multi-select/multi-select.component.ts""""
    )


    //-------Store-------
    //action.constant
    val actionConstantTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "action.constant.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"action.constant.ts"""",
            templatePath = """"angular/store/actions/action.constant.ts""""
    )


    //actions
    val actionsTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "actions.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"actions.ts"""",
            templatePath = """"angular/store/actions/actions.ts""""
    )

    //add.action

    val addActionTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "add.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"add.action.ts"""",
            templatePath = """"angular/store/actions/add.action.ts""""
    )

    //delete.action
    val deleteActionTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "add.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"delete.action.ts"""",
            templatePath = """"angular/store/actions/delete.action.ts""""
    )

    //fetch.action
    val fetchActionTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "add.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"fetch.action.ts"""",
            templatePath = """"angular/store/actions/fetch.action.ts""""
    )

    //set.action
    val setActionTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "set.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"set.action.ts"""",
            templatePath = """"angular/store/actions/set.action.ts""""
    )

    //update.action
    val updateActionTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "update.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"update.action.ts"""",
            templatePath = """"angular/store/actions/update.action.ts""""
    )

    //store.action
    val storeActionTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "store.action.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
            taskType = "multiple",
            filename = """"store.action.ts"""",
            templatePath = """"angular/store/actions/store.action.ts""""
    )

    //interfaces
    //feature-state.interface.ts
    val featureStateInterfaceTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "feature-state.interface.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/interfaces" """,
            taskType = "multiple",
            filename = """"feature-state.interface.ts"""",
            templatePath = """"angular/store/interfaces/feature-state.interface.ts""""
    )

    //interfaces
    //feature-state.interface.ts
    val stateInterfaceTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "state.interface.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store/interfaces" """,
            taskType = "multiple",
            filename = """"state.interface.ts"""",
            templatePath = """"angular/store/interfaces/state.interface.ts""""
    )

    //effects.ts
    val effectsTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "effects.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
            taskType = "multiple",
            filename = """"effects.ts"""",
            templatePath = """"angular/store/effects.ts""""
    )

    //initial-state.constant.ts
    val initialStateConstantTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "initial-state.constant.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
            taskType = "multiple",
            filename = """"initial-state.constant.ts"""",
            templatePath = """"angular/store/initial-state.constant.ts""""
    )

    //reducers.ts
    val reducersTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "reducers.ts",
            folder = """ "pages/" + com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
            taskType = "multiple",
            filename = """"reducers.ts"""",
            templatePath = """"angular/store/reducers.ts""""
    )

    //app-store
    //app-reducers.constant.ts
    val appReducersConstantTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "app-reducers.constant.ts",
            folder = """"app-store/"""",
            taskType = "single",
            filename = """"app-reducers.constant.ts"""",
            templatePath = """"angular/store/app-store/app-reducers.constant.ts""""
    )

    //app-reducers.constant.ts
    val appStateInterfaceTsService = Task(
            taskOfProject = TaskOfProject.UI,
            name = "app-state.interface.ts",
            folder = """"app-store/"""",
            taskType = "single",
            filename = """"app-state.interface.ts"""",
            templatePath = """"angular/store/app-store/app-state.interface.ts""""
    )

   //navigation.component.html.ftl
    val navigationHtml = Task(
            taskOfProject = TaskOfProject.UI,
            name = "navigationHtml",
            folder = """"shared/layout/navigation"""",
            taskType = "single",
            filename = """"navigation.component.html"""",
            templatePath = """"angular/navigation.component.html.ftl""""
    )
    return listOf(entityFormComponentTs, entityFormComponentHtml, entityListComponentTs,
            entityListComponentHtml, SortColums, entityService, entityModule,
            entityModel, pageRouting, appModule,
            effectsTsService,
            appComponentTs,sharedModuleTsService,multiSelectTsService,actionsTsService,addActionTsService,
            deleteActionTsService,
            multiSelectHtmlService,
            fetchActionTsService,
            setActionTsService,
            updateActionTsService,
            storeActionTsService,
            stateInterfaceTsService,
            actionConstantTsService,
            featureStateInterfaceTsService,
            initialStateConstantTsService,
            reducersTsService,
            appReducersConstantTsService,
            appStateInterfaceTsService,
            navigationHtml)
}

