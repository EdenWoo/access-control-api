package com.github.leon.aci.security.handlers

import com.github.leon.aci.exceptions.ApiResp
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MyAccessDeniedHandler : AccessDeniedHandler {
    @Autowired
    private val objectMapper: ObjectMapper? = null

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {

        val apiResp = ApiResp()
        apiResp.error = "AccessDenied: " + request.requestURI
        val msg = objectMapper!!.writeValueAsString(apiResp)
        response.status = 403
        response.writer.write(msg)

    }
}
