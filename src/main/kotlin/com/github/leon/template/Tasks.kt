package com.github.leon.template


import com.github.leon.generator.entity.Task
import com.github.leon.generator.entity.TaskOfProject
import com.github.leon.generator.findClasses

import java.io.File
import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
}

fun uiTemplateTasks(): List<Task> {
    return emptyList()
}

fun apiTasks(): List<Task> {
    val rootPath = Thread.currentThread().contextClassLoader.getResource("")!!.path
    val appConfigPath = "${File(rootPath).parent}/resources/generator/local.properties"
    val appProps = Properties()
    appProps.load(FileInputStream(appConfigPath))
    TaskConstants.init()
    val pattern = "classpath*:com/github/leon/template/task/*/*.class"
    val list = findClasses(Task::class.java, pattern)
            .map {
                (it.newInstance() as Task)
            }.filter { it.taskOfProject == TaskOfProject.API }
    println(list)
    return list

}

fun uiTasks(): List<Task> {
    TaskConstants.init()
    val pattern = "classpath*:com/github/leon/template/task/*/*.class"
    val list = findClasses(Task::class.java, pattern)
            .map {
                (it.newInstance() as Task)
            }.filter { it.taskOfProject == TaskOfProject.UI }
    println(list)
    return list
}


fun testTasks(): List<Task> {
    TaskConstants.init()
    val pattern = "classpath*:com/github/leon/template/task/*/*.class"
    val list = findClasses(Task::class.java, pattern)
            .map {
                (it.newInstance() as Task)
            }.filter { it.taskOfProject == TaskOfProject.TEST }
    println(list)
    return list
}