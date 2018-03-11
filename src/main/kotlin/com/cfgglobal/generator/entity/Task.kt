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
        //log.debug("task " + this.name + " run")
        val paths = taskProcessor(this.taskType).run(codeProject, this, root)
        //log.debug("task " + this.name + " end")
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


    // entity-scanForCodeEntities.component.ts
    val entityListComponentTs = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-scanForCodeEntities.component.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-scanForCodeEntities"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-scanForCodeEntities.component.ts"""",
            templatePath = """"angular/entity-scanForCodeEntities/entity-list_component_ts.ftl""""
    )


    // entity-scanForCodeEntities.component.html
    val entityListComponentHtml = Task(
            taskOfProject = TaskOfProject.UI,
            name = "entity-scanForCodeEntities.component.html",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-scanForCodeEntities"""",
            taskType = "multiple",
            filename = """com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-scanForCodeEntities.component.html"""",
            templatePath = """"angular/entity-scanForCodeEntities/entity-list_component_html.ftl""""
    )


    // sort.columns.ts
    val SortColums = Task(
            taskOfProject = TaskOfProject.UI,
            name = "sort.columns.ts",
            folder = """"pages/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name)+"-scanForCodeEntities"""",
            taskType = "multiple",
            filename = """"sort.columns.ts"""",
            templatePath = """"angular/entity-scanForCodeEntities/sort_columns_ts.ftl""""
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
    return listOf(entityFormComponentTs, entityFormComponentHtml, entityListComponentTs,
            entityListComponentHtml, SortColums, entityService, entityModule,
            entityModel, pageRouting, appModule)
}

