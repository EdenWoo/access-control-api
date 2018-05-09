package com.github.leon.generator.task.processor

import arrow.core.toOption
import com.github.leon.extentions.println
import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.Task
import com.github.leon.generator.task.service.TaskService
import com.google.common.collect.Lists
import org.apache.commons.beanutils.BeanUtils

class MultipleTaskProcessor : ITaskProcessor {
    override fun run(codeProject: CodeProject, task: Task, context: MutableMap<String, Any>): List<String> {
        val paths = Lists.newArrayList<String>()
        for (codeEntity in codeProject.entities) {
            val codeEntityMap = BeanUtils.describe(codeEntity)
            codeEntityMap.println()
            task.entityExtProcessor.toOption().forEach {
                codeEntityMap.putAll(it.invoke(codeEntity))
            }
            codeEntityMap.println()
            task.templateHelper!!.put("entity", codeEntity)
            context["entity"] = codeEntity

            paths.add(TaskService.processTemplate(codeProject, task, context))
        }
        return paths
    }
}
