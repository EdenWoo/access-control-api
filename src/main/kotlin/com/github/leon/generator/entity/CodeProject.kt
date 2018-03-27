package com.github.leon.generator.entity

import com.github.leon.generator.core.ScriptHelper
import com.github.leon.generator.core.TemplateHelper
import com.github.leon.generator.task.service.TaskService

data class CodeProject(
        var packageName: String,

        var templatePath: String,

        var scriptHelper: ScriptHelper,

        var templateEngine: TemplateHelper,

        var entities: List<CodeEntity> = listOf(),

        val enums: List<CodeEnum> = listOf(),

        var utilClasses: List<Class<*>> = listOf(),

        var apiTasks: List<Task> = listOf(),

        var apiTargetPath: String,

        var uiTargetPath: String,

        var uiTasks: List<Task> = listOf(),

        var testTargetPath: String,

        var testTasks: List<Task> = listOf()

) {
    fun generate() {
        (apiTasks + uiTasks + testTasks).parallelStream().forEach {
            TaskService.processTask(this, it)
        }

    }
}

