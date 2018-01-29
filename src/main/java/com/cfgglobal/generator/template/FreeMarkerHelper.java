package com.cfgglobal.generator.template;


import com.cfgglobal.generator.core.TemplateHelper;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;


public class FreeMarkerHelper extends TemplateHelper {
    protected Configuration freeMarkerEngine;
    protected SimpleHash context;

    public FreeMarkerHelper(String templatesBaseDir) {
        freeMarkerEngine = new Configuration(Configuration.VERSION_2_3_21);
        context = new SimpleHash();
        TemplateLoader loader;
        try {
            loader = new FileTemplateLoader(new File(templatesBaseDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        freeMarkerEngine.setTemplateLoader(loader);
    }

    @Override
    public void put(String key, Object value) {
        if (value instanceof Map) {
            value = new SimpleHash((Map) value);
        }
        context.put(key, value);
    }

    @Override
    public void putAll(Map<String, Object> map) {
        for (String key : map.keySet()) {
            put(key, map.get(key));
        }
    }

    @Override
    public void exec(String templateFilename, String targetFilename) {
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilename), "UTF-8"));
            Template template = freeMarkerEngine.getTemplate(templateFilename, "UTF-8");
            template.process(context, out);
        } catch (Exception e) {
            throw new RuntimeException("parse template file [" + templateFilename + "] error", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
