package com.cfgglobal.test.web.interceptors


import com.cfgglobal.test.config.json.JsonConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JsonRenderInterceptor : HandlerInterceptor {

    val log = LoggerFactory.getLogger(JsonRenderInterceptor::class.java)!!
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val uri = request.requestURI
        val embedded = request.getParameter("embedded")
        val fields = request.getParameter("fields")
        val jsonConfig = JsonConfig.create(uri, fields, embedded)
        if (jsonConfig.isDefined()) {
            log.debug("Json Config " + jsonConfig.get().items)
            jsonConfig.get().end()
        }
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {

    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {

    }

}
