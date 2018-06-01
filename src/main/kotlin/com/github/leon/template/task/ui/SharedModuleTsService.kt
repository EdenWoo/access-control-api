package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class SharedModuleTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "shared-module.component.ts",
        folder = """ "shared-module"""",
        taskType = "single",
        filename = """"shared.generated-components.ts"""",
        templatePath = """"angular/shared-module/shared_generated-components_ts.ftl""""
)