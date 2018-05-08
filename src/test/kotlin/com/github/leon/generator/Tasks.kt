package com.github.leon.generator

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject


fun apiTasks(): List<Task> {


    val baseDaoTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "DAO",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"dao/base" """,
            taskType = "multiple",
            filename = """ "Base"+entity.name+"Dao.kt" """,
            templatePath = """ "kotlin/baseDao.ftl" """
    )

    val daoTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "DAO",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"dao" """,
            taskType = "multiple",
            filename = """ entity.name+"Dao.kt" """,
            templatePath = """ "kotlin/dao.ftl" """
    )

    val baseServiceTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "baseService",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"service/base" """,
            taskType = "multiple",
            filename = """ "Base"+entity.name+"Service.kt" """,
            templatePath = """ "kotlin/baseService.ftl" """
    )

    val serviceTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "baseService",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"service" """,
            taskType = "multiple",
            filename = """ entity.name+"Service.kt" """,
            templatePath = """ "kotlin/service.ftl" """
    )


    val baseControllerTask = Task(
            taskOfProject = TaskOfProject.API,
            name = "BaseController",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"controller/base" """,
            taskType = "multiple",
            filename = """ "Base"+entity.name+"Controller.kt" """,
            templatePath = """ "kotlin/baseController.ftl" """
    )

    val controllerTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "BaseController",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"controller" """,
            taskType = "multiple",
            filename = """entity.name+"Controller.kt" """,
            templatePath = """ "kotlin/controller.ftl" """
    )

    val excelExportTask = Task(
            active = false,
            taskOfProject = TaskOfProject.API,
            name = "SERVICE",
            folder = """ "/src/main/kotlin/"+project.packageName.replaceAll("\\.","/")+"/"+"excel" """,
            taskType = "multiple",
            filename = """ entity.name+"ExcelParsingRule.kt" """,
            templatePath = """ "kotlin/excelParsingRule.ftl" """
    )


    val permissionTask = Task(
            replaceFile = false,
            taskOfProject = TaskOfProject.API,
            name = "PERMISSION",
            folder = """ "/"+"db" """,
            taskType = "multiple",
            filename = """ com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-permission.sql" """,
            templatePath = """ "kotlin/permission.ftl" """
    )


    return listOf(baseDaoTask, daoTask, baseServiceTask, serviceTask, baseControllerTask, controllerTask, excelExportTask, permissionTask)

}
