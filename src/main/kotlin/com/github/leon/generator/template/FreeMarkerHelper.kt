package com.github.leon.generator.template


import com.github.leon.generator.core.TemplateHelper
import freemarker.cache.FileTemplateLoader
import freemarker.cache.TemplateLoader
import freemarker.template.Configuration
import freemarker.template.SimpleHash
import java.io.*


class FreeMarkerHelper(templatesBaseDir: String) : TemplateHelper() {
    protected var freeMarkerEngine: Configuration = Configuration(Configuration.VERSION_2_3_21)
    protected var context: SimpleHash = SimpleHash()

    init {
        val loader  = FileTemplateLoader(File(templatesBaseDir))
        freeMarkerEngine.templateLoader = loader
    }

    override fun put(key: String, value: Any?) {
        var value = value
        if (value is Map<*, *>) {
            value = SimpleHash(value)
        }
        context.put(key, value)
    }

    override fun putAll(map: MutableMap<String, Any?>) {
        for (key in map.keys) {
            put(key, map[key] as Any)
        }
    }

    override fun exec(templateFilename: String, targetFilename: String) {
        var out: Writer? = null
        try {
            out = BufferedWriter(OutputStreamWriter(FileOutputStream(targetFilename), "UTF-8"))
            val template = freeMarkerEngine.getTemplate(templateFilename, "UTF-8")
            template.process(context, out)
        } catch (e: Exception) {
            throw RuntimeException("parse template file [$templateFilename] error", e)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

            }
        }
    }


}
