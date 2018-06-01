package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityListComponentTs : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-list.component.ts",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
        taskType = "multiple",
        filename = """com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-list.component.ts"""",
        templatePath = """"angular/entity-list/entity-list_component_ts.ftl""""
)