package com.github.leon.excel.service

import com.github.leon.files.parser.FileParser


interface ExcelParsingRule<T> {


    val fileParser: FileParser

    val entityClass: Class<*>

    val ruleName: String

    fun process(data: List<T>)
}
