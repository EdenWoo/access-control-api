package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class UpdateActionTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "update.action.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"update.action.ts"""",
        templatePath = """"angular/store/actions/update.action.ts"""",
        active = false
)