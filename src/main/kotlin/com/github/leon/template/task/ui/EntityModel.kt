package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityModel : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-model.component.html",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)""",
        taskType = "multiple",
        filename = """com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+".model.ts"""",
        templatePath = """"angular/entity_model_ts.ftl""""
)