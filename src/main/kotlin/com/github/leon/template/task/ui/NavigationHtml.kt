package com.github.leon.template.task.ui

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class NavigationHtml : Task(
        taskOfProject = TaskOfProject.UI,
        name = "navigationHtml",
        folder = """"shared/layout/navigation"""",
        taskType = "single",
        filename = """"navigation.component.html"""",
        templatePath = """"angular/navigation_component_html.ftl""""
)