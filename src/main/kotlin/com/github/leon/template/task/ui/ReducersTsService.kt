package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class ReducersTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "reducers.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
        taskType = "multiple",
        filename = """"reducers.ts"""",
        templatePath = """"angular/store/reducers.ts"""",
        active = false
)