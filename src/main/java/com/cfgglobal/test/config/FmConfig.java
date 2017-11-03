package com.cfgglobal.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Properties;

@Configuration
public class FmConfig {


    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(false);
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer conf = new FreeMarkerConfigurer();
        conf.setTemplateLoaderPaths("classpath:/templates");
        conf.setPreferFileSystemAccess(false);
        // configuration.setTemplateLoaderPaths("/", "classpath:/");

        Properties settings = new Properties();
        settings.setProperty("template_update_delay", "0");
        settings.setProperty("default_encoding", "UTF-8");
        settings.setProperty("tag_syntax", "auto_detect");
        settings.setProperty("whitespace_stripping", "true");
        settings.setProperty("classic_compatible", "true");
        settings.setProperty("object_wrapper", "freemarker.ext.beans.BeansWrapper");
        settings.setProperty("boolean_format", "true,false");
        settings.setProperty("number_format", "0.######");
        settings.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
        settings.setProperty("date_format", "yyyy-MM-dd");
        settings.setProperty("time_format", "HH:mm:ss");

        conf.setFreemarkerSettings(settings);
        return conf;
    }
}
