package com.cfgglobal.generator.core

interface ScriptHelper {
    fun <T> exec(express: String, context: Map<String, Any>): T
}