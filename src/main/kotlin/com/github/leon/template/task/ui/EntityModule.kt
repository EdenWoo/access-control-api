package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityModule : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-module.component.html",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)""",
        taskType = "multiple",
        filename = """com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+".module.ts" """,
        templatePath = """"angular/entity_module_ts.ftl""""
)