package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class StateInterfaceTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "state.interface.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store/interfaces" """,
        taskType = "multiple",
        filename = """"state.interface.ts"""",
        templatePath = """"angular/store/interfaces/state.interface.ts"""",
        active = false
)