package com.cfgglobal.test.config;

import com.cfgglobal.test.config.json.JsonReturnHandler;
import com.cfgglobal.test.domain.convoerters.CustomerConverter;
import com.cfgglobal.test.domain.convoerters.ZonedDateTimeConverter;
import com.cfgglobal.test.web.interceptors.JsonRenderInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.jackson.datatype.VavrModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.ZoneId;
import java.util.List;

//@EnableWebMvc
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebConfig extends WebMvcConfigurerAdapter {


    @Autowired
    JsonReturnHandler jsonReturnHandler;

    @Autowired
    JsonRenderInterceptor jsonRenderInterceptor;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true);
        resolver.setFallbackPageable(new PageRequest(0, 20, Sort.Direction.DESC, "id"));
        argumentResolvers.add(resolver);
        // argumentResolvers.add(new FilterBeanResolver(false));
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ZonedDateTimeConverter(ZoneId.systemDefault()));
        registry.addConverter(new CustomerConverter());

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jsonRenderInterceptor);
        //   registry.addInterceptor(globalParameterInterceptor);
        //  registry.addInterceptor(actionReportInterceptor);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController("/documentation/configuration/ui", "/configuration/ui");
        registry.addRedirectViewController("/documentation/configuration/security", "/configuration/security");
        registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
        registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/documentation", "/documentation/swagger-ui.html");
        registry.addRedirectViewController("/documentation/", "/documentation/swagger-ui.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/documentation/**").addResourceLocations("classpath:/META-INF/resources/");

    }
/*    @Bean
    public BeanNameViewResolver beanNameResolver() {
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(100);
        return beanNameViewResolver;
    }*/

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule());
        //objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());

      /* super.configureMessageConverters(converters);
        // 1.需要先定义一个vonvert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 2.添加fastjson的配置信息，比如：是否格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect);
        SimplePropertyPreFilter exclude = new SimplePropertyPreFilter();
       // exclude.setMaxLevel(2);
       // fastJsonConfig.getClassSerializeFilters().put(BaseEntity.class,exclude);
        // 3.在conver中添加配置信息
        fastJsonConfig.getSerializeConfig().addFilter(Role.class,exclude);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        // 4.将vonver添加到converters当中
        converters.add(fastConverter);*/
    }


    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(jsonReturnHandler);
    }

}