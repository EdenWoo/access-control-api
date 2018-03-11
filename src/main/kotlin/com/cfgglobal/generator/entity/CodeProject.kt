package com.cfgglobal.generator.entity

import com.cfgglobal.generator.core.ScriptHelper
import com.cfgglobal.generator.core.TemplateHelper
import com.cfgglobal.generator.task.service.TaskService
import java.io.File

data class CodeProject(
        var packageName: String,

        var templatePath: String,

        var scriptHelper: ScriptHelper,

        var templateEngine: TemplateHelper,

        var entities: List<CodeEntity> = listOf(),

        var utilClasses: List<Class<*>> = listOf(),

        var apiTasks: List<Task> = listOf(),

        var apiTargetPath: String,

        var uiTargetPath: String,

        var uiTasks: List<Task> = listOf()

){
    fun generate(){
        this.apiTasks.forEach {
            TaskService.processTask(this,it)
        }

        this.uiTasks.forEach {
            TaskService.processTask(this,it)
        }
    }
}

