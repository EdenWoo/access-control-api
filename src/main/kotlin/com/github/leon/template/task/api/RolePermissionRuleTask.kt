package com.github.leon.template.task.api

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import com.github.leon.template.TaskConstants
import com.github.leon.template.entityPermissionProcessor
import com.github.leon.template.projectPermissionProcessor

class RolePermissionRuleTask : Task(
        replaceFile = true,
        active = true,
        taskOfProject = TaskOfProject.API,
        name = "ROLE_PERMISSION_RULE",
        folder = """ "${TaskConstants.generatedPath}"+"/"+"db/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/" """,
        taskType = "multiple",
        filename = """ com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-role-permission-rule.sql" """,
        templatePath = """ "kotlin/rolePermissionRule.ftl" """,
        projectExtProcessor = projectPermissionProcessor,
        entityExtProcessor = entityPermissionProcessor
)