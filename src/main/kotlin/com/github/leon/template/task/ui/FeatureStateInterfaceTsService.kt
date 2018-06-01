package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class FeatureStateInterfaceTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "feature-state.interface.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/interfaces" """,
        taskType = "multiple",
        filename = """"feature-state.interface.ts"""",
        templatePath = """"angular/store/interfaces/feature-state.interface.ts"""",
        active = false
)