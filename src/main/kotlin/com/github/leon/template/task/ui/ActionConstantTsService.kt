package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class ActionConstantTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "action.constant.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"action.constant.ts"""",
        templatePath = """"angular/store/actions/action.constant.ts"""",
        active = false
)