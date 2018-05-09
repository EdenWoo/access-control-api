package com.github.leon.generator.task.service

import arrow.core.toOption
import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import com.google.common.collect.Maps
import freemarker.ext.beans.BeansWrapper
import freemarker.template.TemplateHashModel
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtils
import java.io.File


object TaskService {

    fun processTask(codeProject: CodeProject, task: Task): List<String> {
        val paths: List<String>
        val scope = Maps.newHashMap<String, Any>()
        val codeProjectMap = PropertyUtils.describe(codeProject)
        task.projectExtProcessor.toOption().forEach {
            codeProjectMap.putAll(it.invoke(codeProject))
        }
        scope["project"] = codeProjectMap
        scope["enums"] = codeProject.enums

        codeProject.utilClasses.forEach {
            val wrapper = BeansWrapper.getDefaultInstance()
            val staticModels = wrapper.staticModels
            val fileStatics = staticModels.get(it.name) as TemplateHashModel
            scope[it.simpleName] = fileStatics
        }
        task.templateHelper = codeProject.templateEngine
        task.templateHelper!!.putAll(scope)
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
            TaskOfProject.UI_TEMPLATE -> codeProject.testTargetPath + File.separator + folder
        }
        val folderDir = File(folder)
        if (!folderDir.exists()) {
            folderDir.mkdirs()
        }
        val filename = codeProject.scriptHelper.exec<Any>(task.filename, root).toString()
        val outputFilename = folder + File.separator + filename
        val outputFile = File(outputFilename)
        if (task.replaceFile || !outputFile.exists()) {
            task.templateHelper!!.exec(templateFilename, outputFilename)
        }
        return outputFilename

    }


}
