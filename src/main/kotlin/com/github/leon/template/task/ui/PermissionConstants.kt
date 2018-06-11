package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class PermissionConstants : Task(
        taskOfProject = TaskOfProject.UI,
        name = "permission-constants",
        folder = """"constants"""",
        taskType = "single",
        filename = """"permission.constant.ts"""",
        templatePath = """"angular/permission/permission.ftl""""
)
