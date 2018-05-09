package com.github.leon.generator


import com.github.leon.classpath.PathKit
import com.github.leon.generator.entity.CodeProject
import com.github.leon.generator.entity.scanForCodeEntities
import com.github.leon.generator.entity.scanForCodeEnum
import com.github.leon.generator.ext.Utils
import com.github.leon.generator.script.DefaultScriptHelper
import com.github.leon.generator.template.FreeMarkerHelper
import java.io.File
import java.io.FileInputStream
import java.util.*


fun main(args: Array<String>) {

    val rootPath = Thread.currentThread().contextClassLoader.getResource("")!!.path
    val appConfigPath = "${File(rootPath).parent}/resources/generator/local.properties"

    val appProps = Properties()
    appProps.load(FileInputStream(appConfigPath))


    val templatePath = File(PathKit.getRootClassPath()).parent + "/resources/templates"

    val apiTargetPath = System.getProperty("user.dir")
    val uiTargetPath = ""
    val testTargetPath = ""
    val uiTemplateTargetPath = ""

    val entities = scanForCodeEntities()
    entities.forEach {
        println(it.name)
        println(it.fields.map { it.name }.toList())
    }
    val enums = scanForCodeEnum()
    CodeProject(
            entities = entities,
            enums = enums,
            utilClasses = listOf(Utils::class.java),
            packageName = "com.github.leon.test",
            templatePath = templatePath,
            testTasks = listOf(),
            uiTasks = listOf(),
            apiTasks = apiTasks(),
            uiTemplateTasks =  listOf(),
            uiTemplateTargetPath = uiTemplateTargetPath,
            uiTargetPath = uiTargetPath,
            apiTargetPath = apiTargetPath,
            testTargetPath = testTargetPath,
            scriptHelper = DefaultScriptHelper("groovy"),
            templateEngine = FreeMarkerHelper(templatePath)
    ).generate()


}


