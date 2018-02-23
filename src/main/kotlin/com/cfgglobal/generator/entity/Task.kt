package com.cfgglobal.generator.entity


import com.cfgglobal.generator.task.processor.ITaskProcessor
import lombok.Data
import org.apache.commons.lang3.StringUtils
import org.joor.Reflect


data  class Task(
        var id: Int? = null,

        var name: String,

        var taskType: String,

        var folder: String,

        var filename: String,

        var templatePath: String
) {

    fun run(codeProject: CodeProject, root: MutableMap<String, Any>): List<String> {
        //log.debug("task " + this.name + " run")
        val paths = taskProcessor(this.taskType).run(codeProject, this, root)
        //log.debug("task " + this.name + " end")
        return paths
    }

    private fun taskProcessor(taskType: String): ITaskProcessor {
        return Reflect.on(ITaskProcessor::class.java.`package`.name + "."
                + StringUtils.capitalize(taskType) +
                "TaskProcessor").create().get()
    }
}
