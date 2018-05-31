package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class InitialStateConstantTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "initial-state.constant.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
        taskType = "multiple",
        filename = """"initial-state.constant.ts"""",
        templatePath = """"angular/store/initial-state.constant.ts"""",
        active = false
)