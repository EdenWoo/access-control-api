package com.github.leon.generator

import com.github.leon.generator.entity.CodeEntity
import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import java.io.File
import java.io.FileInputStream
import java.util.*


fun apiTasks(): List<Task> {

    val rootPath = Thread.currentThread().contextClassLoader.getResource("")!!.path
    val appConfigPath = "${File(rootPath).parent}/resources/generator/local.properties"

    val appProps = Properties()
    appProps.load(FileInputStream(appConfigPath))


    val generatedPath = "/"
    val apiPath = "/"
    val srcPath = "/generated/"


    val projectExtProcessor: (CodeProject) -> Map<String, String> = {
        mapOf("projectExt" to it.apiTargetPath)
    }

    val entityExtProcessor: (CodeEntity) -> Map<String, String> = {
        mapOf(
                "entityExt" to it.code.inc().toString()
        )
    }

    val baseDaoTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "DAO",
            folder = """ "$generatedPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"dao/base" """,
            taskType = "multiple",
            filename = """ "Base"+entity.name+"Dao.kt" """,
            templatePath = """ "kotlin/baseDao.ftl" """,
            projectExtProcessor = projectExtProcessor,
            entityExtProcessor = entityExtProcessor
    )

    val daoTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "DAO",
            folder = """ "$apiPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"dao" """,
            taskType = "multiple",
            filename = """ entity.name+"Dao.kt" """,
            templatePath = """ "kotlin/dao.ftl" """,

    )

    val baseServiceTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "baseService",
            folder = """ "$generatedPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"service/base" """,
            taskType = "multiple",
            filename = """ "Base"+entity.name+"Service.kt" """,
            templatePath = """ "kotlin/baseService.ftl" """
    )

    val serviceTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "baseService",
            folder = """ "$apiPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"service" """,
            taskType = "multiple",
            filename = """ entity.name+"Service.kt" """,
            templatePath = """ "kotlin/service.ftl" """
    )


    val baseControllerTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "BaseController",
            folder = """ "$generatedPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"controller/base" """,
            taskType = "multiple",
            filename = """ "Base"+entity.name+"Controller.kt" """,
            templatePath = """ "kotlin/baseController.ftl" """
    )

    val controllerTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "BaseController",
            folder = """ "$apiPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"controller" """,
            taskType = "multiple",
            filename = """entity.name+"Controller.kt" """,
            templatePath = """ "kotlin/controller.ftl" """
    )

    val excelExportTask = Task(
            active = false,
            taskOfProject = TaskOfProject.API,
            name = "SERVICE",
            folder = """ "$generatedPath"+"$srcPath"+project.packageName.replaceAll("\\.","/")+"/"+"excel" """,
            taskType = "multiple",
            filename = """ entity.name+"ExcelParsingRule.kt" """,
            templatePath = """ "kotlin/excelParsingRule.ftl" """
    )



    return listOf(baseDaoTask, daoTask, baseServiceTask, serviceTask, baseControllerTask, controllerTask, excelExportTask)

}
