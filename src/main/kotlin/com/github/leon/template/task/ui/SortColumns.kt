package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class SortColumns : Task(
        taskOfProject = TaskOfProject.UI,
        name = "sort.columns.ts",
        folder = """"pages/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"/"+com.github.leon.generator.ext.Utils.lowerHyphen(entity.name)+"-list"""",
        taskType = "multiple",
        filename = """"sort.columns.ts"""",
        templatePath = """"angular/entity-list/sort_columns_ts.ftl""""
)