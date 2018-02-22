package com.cfgglobal.test.config.json

import io.vavr.collection.List
import org.joor.Reflect
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JsonReturnHandler : HandlerMethodReturnValueHandler, BeanPostProcessor {


    internal var advices = List.empty<ResponseBodyAdvice<Any>>()

    override fun supportsReturnType(returnType: MethodParameter): Boolean {

        val obj = Reflect.on(returnType).get<Any>("returnValue")
        if (obj is ResponseEntity<*>) {
            return JsonConfig.get().isDefined
        } else {
            if (JsonConfig.get().isDefined) {
                //     Object view = Reflect.on(obj).get("view");
                System.err.println("json render exception: " + returnType + ", " + JsonConfig.get().get().items)
            }
            return false
        }

    }

    @Throws(Exception::class)
    override fun handleReturnValue(returnValue: Any?, returnType: MethodParameter, mavContainer: ModelAndViewContainer,
                                   webRequest: NativeWebRequest) {
        var returnValue = returnValue
        mavContainer.isRequestHandled = true
        for (ad in advices) {
            if (ad.supports(returnType, null!!)) {
                returnValue = ad.beforeBodyWrite(returnValue, returnType, MediaType.APPLICATION_JSON_UTF8, null!!,
                        ServletServerHttpRequest(webRequest.getNativeRequest(HttpServletRequest::class.java)!!),
                        ServletServerHttpResponse(webRequest.getNativeResponse(HttpServletResponse::class.java)!!))
            }
        }

        val response = webRequest.getNativeResponse(HttpServletResponse::class.java)

        val jsonSerializer = CustomerJsonSerializer()

        JsonConfig
                .get()
                .map({ it.items })
                .getOrElse(mutableListOf())
                .forEach { jsonSerializer.filter(it) }

        response!!.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
        if (returnValue!!.javaClass == ResponseEntity::class.java) {
            returnValue = Reflect.on(returnValue).get<Any>("body")
        }

        val json = jsonSerializer.toJson(returnValue)
        response.writer.write(json)
    }

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {

        if (bean is ResponseBodyAdvice<*>) {
            advices = advices.append(bean as ResponseBodyAdvice<Any>)
        } else if (bean is RequestMappingHandlerAdapter) {
            println(bean.javaClass.toString() + "  " + beanName)
            var handlers = List.ofAll(bean.returnValueHandlers!!)
            var jsonHandler: JsonReturnHandler? = null
            for (i in 0 until handlers.size()) {
                val handler = handlers.get(i)
                if (handler is JsonReturnHandler) {
                    jsonHandler = handler
                    break
                }
            }
            if (jsonHandler != null) {
                handlers = handlers.remove(jsonHandler)
                handlers = handlers.prepend(jsonHandler)
                bean.returnValueHandlers = handlers.toJavaList() // change the jsonhandler sort
            }
        }
        return bean
    }

}