package com.github.leon.fm


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import java.io.StringWriter


@Component
class FreemarkerBuilderUtil(
        @Autowired
        val freemarkerCfg: FreeMarkerConfigurer
) {


    fun build(ftlPath: String, model: Map<String, Any?>): String? {

        val writer = StringWriter()
        val template = freemarkerCfg.configuration.getTemplate(ftlPath)
        template.process(model, writer)
        return writer.toString()

    }
}
