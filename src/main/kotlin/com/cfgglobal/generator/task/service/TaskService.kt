package com.cfgglobal.generator.task.service

import com.cfgglobal.generator.entity.CodeProject
import com.cfgglobal.generator.entity.Task
import com.google.common.collect.Maps
import freemarker.ext.beans.BeansWrapper
import freemarker.template.TemplateHashModel
import freemarker.template.TemplateModelException
import java.io.File


object TaskService {

    fun processTask(codeProject: CodeProject, task: Task): List<String> {
        val paths: List<String>
        val entities = codeProject.entities
        val scope = Maps.newHashMap<String, Any>()
        scope["project"] = codeProject
        scope["entities"] = entities
        codeProject.utilClasses!!.forEach { util ->


            val wrapper = BeansWrapper.getDefaultInstance()
            val staticModels = wrapper.staticModels
            try {
                val fileStatics = staticModels.get(util.name) as TemplateHashModel
                scope[util.simpleName] = fileStatics
            } catch (e: TemplateModelException) {
                e.printStackTrace()
            }


        }

        codeProject.templateEngine.putAll(scope)
        paths = task.run(codeProject, scope)
        return paths
    }

    /**
     * @param root
     * @param task
     * @return 生成文件的路径
     */
    fun processTemplate(codeProject: CodeProject, task: Task, root: Map<String, Any>): String {
        /*    List<TaskParam> params = task.getTaskParams();
        for (TaskParam param : params) {
            if (StrKit.isBlank(param.getStr("name"))) continue;
            String value = param.getStr("expression");
            Config.templateEngine().put(param.getStr("name"), Config.scriptHelper().exec(value, root));
        }*/
        val templateFilename = codeProject.scriptHelper.exec<Any>(task.templatePath, root).toString()
        var folder = codeProject.scriptHelper.exec<Any>(task.folder, root).toString()
        folder = codeProject.targetPath + File.separator + folder
        val folderDir = File(folder)
        if (!folderDir.exists()) {
            folderDir.mkdirs()
        }
        val filename = codeProject.scriptHelper.exec<Any>(task.filename, root).toString()
        val outputFilename = folder + File.separator + filename
        // log.debug(outputFilename)
        codeProject.templateEngine.exec(templateFilename, outputFilename)
        return outputFilename

    }


}
