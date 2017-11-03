package com.cfgglobal.test.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class ServeletConfig {


    @Bean
    public FilterRegistrationBean hiddenMethod() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        registrationBean.setFilter(hiddenHttpMethodFilter);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean charset() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }

}
