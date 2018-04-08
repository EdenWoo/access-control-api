package com.github.leon.generator.task.service

import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import com.google.common.collect.Maps
import freemarker.ext.beans.BeansWrapper
import freemarker.template.TemplateHashModel
import java.io.File


object TaskService {

    fun processTask(codeProject: CodeProject, task: Task): List<String> {
        val paths: List<String>
        val scope = Maps.newHashMap<String, Any>()
        scope["project"] = codeProject
        scope["entities"] = codeProject.entities
        scope["enums"] = codeProject.templateEngine
        codeProject.utilClasses.forEach { util ->
            val wrapper = BeansWrapper.getDefaultInstance()
            val staticModels = wrapper.staticModels
            val fileStatics = staticModels.get(util.name) as TemplateHashModel
            scope[util.simpleName] = fileStatics
        }
        codeProject.templateEngine.putAll(scope)
        paths = task.run(codeProject, scope)
        return paths
    }

    fun processTemplate(codeProject: CodeProject, task: Task, root: Map<String, Any>): String {
        /*    List<TaskParam> params = task.getTaskParams();
        for (TaskParam param : params) {
            if (StrKit.isBlank(param.getStr("name"))) continue;
            String value = param.getStr("expression");
            Config.templateEngine().put(param.getStr("name"), Config.scriptHelper().exec(value, root));
        }*/
        val templateFilename = codeProject.scriptHelper.exec<Any>(task.templatePath, root).toString()
        var folder = codeProject.scriptHelper.exec<Any>(task.folder, root).toString()
        folder = when (task.taskOfProject) {
            TaskOfProject.API -> codeProject.apiTargetPath + File.separator + folder
            TaskOfProject.UI -> codeProject.uiTargetPath + File.separator + folder
            TaskOfProject.TEST -> codeProject.testTargetPath + File.separator + folder
        }
        val folderDir = File(folder)
        if (!folderDir.exists()) {
            folderDir.mkdirs()
        }
        val filename = codeProject.scriptHelper.exec<Any>(task.filename, root).toString()
        val outputFilename = folder + File.separator + filename
        val outputFile = File(outputFilename)
        if (task.replaceFile || !outputFile.exists()) {
            codeProject.templateEngine.exec(templateFilename, outputFilename)
        }
        return outputFilename

    }


}
