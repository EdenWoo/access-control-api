package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class StoreActionTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "store.action.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/actions" """,
        taskType = "multiple",
        filename = """"store.action.ts"""",
        templatePath = """"angular/store/actions/store.action.ts"""",
        active = false
)