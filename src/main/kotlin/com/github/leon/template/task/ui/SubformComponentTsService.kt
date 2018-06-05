package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class subformComponentTsService : Task(
        taskOfProject = TaskOfProject.UI,
        name = "subform-component.ts",
        folder = """ "shared-module/subform-components/" + com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "/" """,
        taskType = "multiple",
        filename = """ com.github.leon.generator.ext.Utils.lowerHyphen(entity.name) + "-subform.component.ts" """,
        templatePath = """"angular/shared-module/subform-component/subform_component_ts.ftl""""
)
