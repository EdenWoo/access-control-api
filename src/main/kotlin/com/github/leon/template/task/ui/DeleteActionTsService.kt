package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class DeleteActionTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "add.action.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"delete.action.ts"""",
        templatePath = """"angular/store/actions/delete.action.ts"""",
        active = false
)