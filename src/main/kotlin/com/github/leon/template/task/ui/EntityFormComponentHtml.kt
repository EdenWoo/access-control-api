package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityFormComponentHtml : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-form.component.html",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-form"""",
        taskType = "multiple",
        filename = """com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-form.component.html"""",
        templatePath = """"angular/entity-form/entity-form_component_html.ftl""""
)