package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class AppModule : Task(
        taskOfProject = TaskOfProject.UI,
        name = "appModule",
        folder = """"/"""",
        taskType = "single",
        filename = """"app.module.ts"""",
        templatePath = """"angular/app_module_ts.ftl""""
)