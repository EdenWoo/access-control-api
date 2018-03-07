package com.cfgglobal.test.config

import com.cfgglobal.test.config.json.JsonReturnHandler
import com.cfgglobal.test.web.interceptors.ActionReportInterceptor
import com.cfgglobal.test.web.interceptors.JsonRenderInterceptor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

//@EnableWebMvc
@Configuration
class WebConfig : WebMvcConfigurerAdapter() {


    @Autowired
    internal var jsonReturnHandler: JsonReturnHandler? = null

    @Autowired
    internal var jsonRenderInterceptor: JsonRenderInterceptor? = null

    @Autowired
    internal var actionReportInterceptor: ActionReportInterceptor? = null


    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        val resolver = PageableHandlerMethodArgumentResolver()
        resolver.setOneIndexedParameters(true)
        resolver.setFallbackPageable(PageRequest(0, 20, Sort.Direction.DESC, "id"))
        argumentResolvers.add(resolver)
        // argumentResolvers.add(new FilterBeanResolver(false));
        super.addArgumentResolvers(argumentResolvers)
    }

    override fun addFormatters(registry: FormatterRegistry) {
        // super.addFormatters(registry);
        // registry.addConverter(new ZonedDateTimeConverter(ZoneId.systemDefault()));
        //  registry.addConverter(new CustomerConverter());

    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jsonRenderInterceptor!!)
        //   registry.addInterceptor(globalParameterInterceptor);
        registry.addInterceptor(actionReportInterceptor!!)
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs")
        registry.addRedirectViewController("/documentation/configuration/ui", "/configuration/ui")
        registry.addRedirectViewController("/documentation/configuration/security", "/configuration/security")
        registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources")
        registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui")
        registry.addRedirectViewController("/documentation", "/documentation/swagger-ui.html")
        registry.addRedirectViewController("/documentation/", "/documentation/swagger-ui.html")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        registry.addResourceHandler("/documentation/**").addResourceLocations("classpath:/META-INF/resources/")

    }
    /*    @Bean
    public BeanNameViewResolver beanNameResolver() {
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(100);
        return beanNameViewResolver;
    }*/

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        val jsonConverter = MappingJackson2HttpMessageConverter()
        val objectMapper = ObjectMapper()
        //objectMapper.findAndRegisterModules();
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        jsonConverter.objectMapper = objectMapper
        return jsonConverter
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(mappingJackson2HttpMessageConverter())

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


    override fun addReturnValueHandlers(returnValueHandlers: MutableList<HandlerMethodReturnValueHandler>) {
        returnValueHandlers.add(jsonReturnHandler!!)
    }

}