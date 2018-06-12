package com.github.leon.template.task.api

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import com.github.leon.template.TaskConstants
import com.github.leon.template.entityPermissionProcessor
import com.github.leon.template.projectPermissionProcessor

class AllPermissionTask : Task(
        replaceFile = true,
        active = false,
        taskOfProject = TaskOfProject.API,
        name = "AllPermission",
        folder = """ "${TaskConstants.generatedPath}"+"/"+"db/" """,
        taskType = "single",
        filename = """ "permission-all.sql" """,
        templatePath = """ "kotlin/allPermission.ftl" """,
        projectExtProcessor = projectPermissionProcessor,
        entityExtProcessor = entityPermissionProcessor
)