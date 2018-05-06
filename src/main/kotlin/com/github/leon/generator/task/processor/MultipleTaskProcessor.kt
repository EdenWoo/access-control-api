package com.github.leon.generator.task.processor

import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.Task
import com.github.leon.generator.task.service.TaskService
import com.google.common.collect.Lists

class MultipleTaskProcessor : ITaskProcessor {
    override fun run(codeProject: CodeProject, task: Task, context: MutableMap<String, Any>): List<String> {
        val paths = Lists.newArrayList<String>()
        for (codeEntity in codeProject.entities) {
            context["entity"] = codeEntity
            task.templateHelper!!.put("entity", codeEntity)
            paths.add(TaskService.processTemplate(codeProject, task, context))
        }
        return paths

    }
}
