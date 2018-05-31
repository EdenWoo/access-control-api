package com.github.leon.generator


import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.scanForCodeEntities
import com.github.leon.generator.entity.scanForCodeEnum
import com.github.leon.generator.ext.Utils
import com.github.leon.generator.script.DefaultScriptHelper
import com.github.leon.generator.template.FreeMarkerHelper
import com.github.leon.template.apiTasks
import com.github.leon.template.uiTasks

import java.io.File
import java.io.FileInputStream
import java.util.*


fun generate() {

    val rootPath = Thread.currentThread().contextClassLoader.getResource("")!!.path
    val appConfigPath = "${File(rootPath).parent}/resources/generator/local.properties"

    val appProps = Properties()
    appProps.load(FileInputStream(appConfigPath))

    val templatePath = System.getProperty("user.dir") + "/task/src/main/resources/templates"

    val packageName = appProps.getProperty("packageName")
    val apiTargetPath = System.getProperty("user.dir")
    val uiTargetPath = appProps.getProperty("uiTargetPath")
    val testTargetPath = appProps.getProperty("testTargetPath")
    val uiTemplateTargetPath = appProps.getProperty("uiTemplateTargetPath")

    val classpathName = "${packageName.replace(".", "/")}/domain/*.class"
    println(classpathName)
    val entities = scanForCodeEntities("classpath*:$classpathName")
    entities.forEach {
        println(it.name)
        println(it.fields.map { it.name }.toList())
    }
    val enums = scanForCodeEnum()
    CodeProject(
            entities = entities,
            enums = enums,
            utilClasses = listOf(Utils::class.java),
            packageName = packageName,
            templatePath = templatePath,
            uiTemplateTargetPath = uiTemplateTargetPath,
            testTasks = listOf(),
            uiTasks = uiTasks(),
            apiTasks = apiTasks(),
            uiTargetPath = uiTargetPath,
            apiTargetPath = apiTargetPath,
            testTargetPath = testTargetPath,
            scriptHelper = DefaultScriptHelper("groovy"),
            templateEngine = FreeMarkerHelper(templatePath)
    ).generate()


}



