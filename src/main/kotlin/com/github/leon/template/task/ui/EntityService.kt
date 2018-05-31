package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class EntityService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "entity-service.component.html",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)""",
        taskType = "multiple",
        filename = """ com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+".service.ts" """,
        templatePath = """"angular/entity_service_ts.ftl""""
)