package com.cfgglobal.generator.task.processor

import com.cfgglobal.generator.entity.CodeProject
import com.cfgglobal.generator.entity.Task
import com.cfgglobal.generator.task.service.TaskService
import com.google.common.collect.Lists

class MultipleTaskProcessor : ITaskProcessor {
    override fun run(codeProject: CodeProject, task: Task, context: MutableMap<String, Any>): List<String> {
        val paths = Lists.newArrayList<String>()
        for (codeEntity in codeProject.entities) {
            context["entity"] = codeEntity
            codeProject.templateEngine.put("entity", codeEntity)
            paths.add(TaskService.processTemplate(codeProject, task, context))
        }
        return paths

    }
}
