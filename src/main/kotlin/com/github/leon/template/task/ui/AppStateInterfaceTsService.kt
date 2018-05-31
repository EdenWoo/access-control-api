package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class AppStateInterfaceTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "app-state.interface.ts",
        folder = """"app-store/"""",
        taskType = "single",
        filename = """"app-state.interface.ts"""",
        templatePath = """"angular/store/app-store/app-state.interface.ts"""",
        active = false
)