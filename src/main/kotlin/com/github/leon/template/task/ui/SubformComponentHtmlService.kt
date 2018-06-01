package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class SubformComponentHtmlService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "subform-component.html",
        folder = """ "shared-module/subform-components/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/" """,
        taskType = "multiple",
        filename = """ com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "-subform.component.html" """,
        templatePath = """"angular/shared-module/subform-component/subform.component_html.ftl""""
)