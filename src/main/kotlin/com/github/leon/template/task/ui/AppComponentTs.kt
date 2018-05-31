package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class AppComponentTs : Task(
        taskOfProject = TaskOfProject.UI,
        name = "app.component.ts",
        folder = """"/"""",
        taskType = "single",
        filename = """"app.component.ts"""",
        templatePath = """"angular/app.component.ts""""
)