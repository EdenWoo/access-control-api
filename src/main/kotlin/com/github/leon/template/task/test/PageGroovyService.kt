package com.github.leon.template.task.test

import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject

class PageGroovyService : Task(
        taskOfProject = TaskOfProject.TEST,
        name = "pageGroovy",
        folder = """"page/"""",
        taskType = "multiple",
        filename = """ com.github.leon.generator.ext.Utils.upperCamel(entity.name) + "Test.groovy" """,
        templatePath = """"test/page/Page_groovy.ftl""""
)