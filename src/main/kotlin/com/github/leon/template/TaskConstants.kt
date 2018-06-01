package com.github.leon.template

import java.io.File
import java.util.*

object TaskConstants {
    lateinit var generatedPath: String
    lateinit var apiPath: String
    lateinit var srcPath: String
    fun init() {
        val classLoader = javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream("generator/local.properties")

        val rootPath = Thread.currentThread().contextClassLoader.getResource("")!!.path
        val appConfigPath = "${File(rootPath).parent}/resources/generator/local.properties"
        val appProps = Properties()
        appProps.load(inputStream)
        //appProps.load(FileInputStream(appConfigPath))
        val projectName = appProps.getProperty("projectName")
        generatedPath = "/$projectName-generated"
        apiPath = "/$projectName-api"
        srcPath = "/src/main/kotlin/"
    }
}