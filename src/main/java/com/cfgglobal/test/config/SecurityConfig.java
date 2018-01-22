package com.cfgglobal.test.config;

import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.security.LogoutSuccess;
import com.cfgglobal.test.security.MyFilterSecurityInterceptor;
import com.cfgglobal.test.security.TokenAuthenticationFilter;
import com.cfgglobal.test.security.handlers.AuthenticationFailureHandler;
import com.cfgglobal.test.security.handlers.AuthenticationSuccessHandler;
import com.cfgglobal.test.security.handlers.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private LogoutSuccess logoutSuccess;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/singleSave", "/admin/**", "/js/**", "/css/**", "/images/**", "/**/favicon.ico");
    }

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        return new TokenAuthenticationFilter();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .addFilter(corsFilter())
                //.addFilterBefore(corsFilter(), SessionManagementFilter.class) //adds your custom CorsFilter
                .addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler).and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(logoutSuccess)
                .deleteCookies();
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);


    }

}
