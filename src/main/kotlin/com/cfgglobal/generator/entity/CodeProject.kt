package com.cfgglobal.generator.entity

import com.cfgglobal.generator.core.ScriptHelper
import com.cfgglobal.generator.core.TemplateHelper
import java.io.File

data class CodeProject(
        var packageName: String,

        var templatePath: String,

        var targetPath: String? = null,

        var scriptHelper: ScriptHelper,

        var templateEngine: TemplateHelper,

        var entities: List<CodeEntity> = listOf(),

        var utilClasses: List<Class<*>>? = listOf()


) {
    companion object {
        private val FS = File.separator
    }

}
