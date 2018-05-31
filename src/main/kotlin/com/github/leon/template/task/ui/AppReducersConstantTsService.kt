package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class AppReducersConstantTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "app-reducers.constant.ts",
        folder = """"app-store/"""",
        taskType = "single",
        filename = """"app-reducers.constant.ts"""",
        templatePath = """"angular/store/app-store/app-reducers.constant.ts"""",
        active = false
)