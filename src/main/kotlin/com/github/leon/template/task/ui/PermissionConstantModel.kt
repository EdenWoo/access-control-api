package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class PermissionConstantModel : Task(
        taskOfProject = TaskOfProject.UI,
        name = "permission-constant-model",
        folder = """"models/bases""",
        taskType = "single",
        filename = """"permission-constant.model.ts"""",
        templatePath = """"angular/permission/permission-constant_model_ts.ftl""""
)

//src/app/models/bases/permission-constant.model.ts