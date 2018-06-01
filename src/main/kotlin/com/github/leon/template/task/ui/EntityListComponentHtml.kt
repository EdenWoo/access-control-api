package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityListComponentHtml : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-list.component.html",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
        taskType = "multiple",
        filename = """com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-list.component.html"""",
        templatePath = """"angular/entity-list/entity-list_component_html.ftl""""
)