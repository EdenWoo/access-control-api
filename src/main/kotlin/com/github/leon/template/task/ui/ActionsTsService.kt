package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class ActionsTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "actions.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"actions.ts"""",
        templatePath = """"angular/store/actions/actions.ts"""",
        active = false
)