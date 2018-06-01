package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class PageRouting : Task(
        taskOfProject = TaskOfProject.UI,
        name = "pagesoutings",
        folder = """"pages/"""",
        taskType = "multiple",
        filename = """"pages.routing.ts"""",
        templatePath = """"angular/pages_routing_ts.ftl""""
)