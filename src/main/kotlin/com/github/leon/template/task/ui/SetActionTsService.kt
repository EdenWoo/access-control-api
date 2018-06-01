package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class SetActionTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "set.action.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"set.action.ts"""",
        templatePath = """"angular/store/actions/set.action.ts"""",
        active = false
)