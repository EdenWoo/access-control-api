package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class AddActionTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "add.action.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"add.action.ts"""",
        templatePath = """"angular/store/actions/add.action.ts"""",
        active = false
)