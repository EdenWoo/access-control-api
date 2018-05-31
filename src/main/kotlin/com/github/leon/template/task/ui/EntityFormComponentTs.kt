package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityFormComponentTs : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-form.component.ts",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-form"""",
        taskType = "multiple",
        filename = """com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-form.component.ts"""",
        templatePath = """"angular/entity-form/entity-form_component_ts.ftl""""
)