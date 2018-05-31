package com.github.leon.template.task.api

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import com.github.leon.template.TaskConstants

class ExcelExportTask : Task(
        active = true,
        taskOfProject = TaskOfProject.API,
        name = "SERVICE",
        folder = """ "${TaskConstants.generatedPath}"+"${TaskConstants.srcPath}"+project.packageName.replaceAll("\\.","/")+"/"+"excel" """,
        taskType = "multiple",
        filename = """ entity.name+"ExcelParsingRule.kt" """,
        templatePath = """ "kotlin/excelParsingRule.ftl" """
)