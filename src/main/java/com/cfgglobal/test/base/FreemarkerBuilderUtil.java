package com.cfgglobal.test.base;


import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;


@Component
public class FreemarkerBuilderUtil {

    @Autowired
    private FreeMarkerConfigurer freemarkerCfg;


    public String build(String ftlPath, Map<String, Object> model) throws IOException {

        StringWriter writer;
        try {
            Template template = freemarkerCfg.getConfiguration().getTemplate(ftlPath);
            writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
