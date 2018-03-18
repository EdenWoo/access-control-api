package com.github.leon.aci.security

import org.springframework.beans.factory.annotation.Value
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.leon.cache.CacheClient
import com.github.leon.security.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component

class LogoutSuccess(

        @Value("\${spring.application.name}")
        val application:String,
        @Autowired
        val objectMapper: ObjectMapper,

        val cacheClient: CacheClient
) : LogoutSuccessHandler {


    @Throws(IOException::class, ServletException::class)
    override fun onLogoutSuccess(httpServletRequest: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        cacheClient.deleteByKey("$application-${authentication.name}")
        val result = HashMap<String, String>()
        result["result"] = "success"
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(result))
        response.status = HttpServletResponse.SC_OK

    }

}