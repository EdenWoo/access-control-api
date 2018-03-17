package com.cfgglobal.test.config

import com.cfgglobal.test.security.LogoutSuccess
import com.cfgglobal.test.security.MyFilterSecurityInterceptor
import com.cfgglobal.test.security.TokenAuthenticationFilter
import com.cfgglobal.test.security.handlers.AuthenticationFailureHandler
import com.cfgglobal.test.security.handlers.AuthenticationSuccessHandler
import com.cfgglobal.test.security.handlers.MyAccessDeniedHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val myFilterSecurityInterceptor: MyFilterSecurityInterceptor? = null

    @Autowired
    private val myAccessDeniedHandler: MyAccessDeniedHandler? = null

    @Autowired
    private val logoutSuccess: LogoutSuccess? = null
    @Autowired
    private val authenticationSuccessHandler: AuthenticationSuccessHandler? = null
    @Autowired
    private val authenticationFailureHandler: AuthenticationFailureHandler? = null
    @Autowired
    private val passwordEncoder: PasswordEncoder? = null

    @Autowired
    lateinit var tokenAuthenticationFilter: TokenAuthenticationFilter
    @Autowired
    internal var userDetailsService: UserDetailsService? = null

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers( "/js/**", "/css/**", "/images/**", "/**/favicon.ico")
    }



    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService<UserDetailsService>(userDetailsService)
                .passwordEncoder(passwordEncoder)

    }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.headers().frameOptions().sameOrigin()
        http.cors()
        http.csrf().disable()
        http.headers().cacheControl().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .addFilter(corsFilter())
                //.addFilterBefore(corsFilter(), SessionManagementFilter.class) //adds your custom CorsFilter
                .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter::class.java)
                .addFilterBefore(myFilterSecurityInterceptor!!, FilterSecurityInterceptor::class.java)
                .authorizeRequests()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler).and()
                .logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(logoutSuccess)
                .deleteCookies()
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)


    }

}
