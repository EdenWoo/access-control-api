package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EffectsTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "effects.ts",
        folder = """ "pages/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/store" """,
        taskType = "multiple",
        filename = """"effects.ts"""",
        templatePath = """"angular/store/effects.ts"""",
        active = false
)