package com.cfgglobal.generator.task.processor


import com.cfgglobal.generator.entity.CodeProject
import com.cfgglobal.generator.entity.Task

interface ITaskProcessor {
    fun run(codeProject: CodeProject, task: Task, context: MutableMap<String, Any>): List<String>
}
