package com.cfgglobal.generator.task.processor

import com.cfgglobal.generator.entity.CodeProject
import com.cfgglobal.generator.entity.Task
import com.cfgglobal.generator.task.service.TaskService
import com.google.common.collect.Lists

class SingleTaskProcessor : ITaskProcessor {
    override fun run(codeProject: CodeProject, task: Task, context: MutableMap<String, Any>): List<String> {
        return Lists.newArrayList(TaskService.processTemplate(codeProject, task, context))

    }
}
