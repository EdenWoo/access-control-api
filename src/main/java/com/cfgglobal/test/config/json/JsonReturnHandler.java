package com.cfgglobal.test.config.json;

import io.vavr.collection.List;
import org.joor.Reflect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JsonReturnHandler implements HandlerMethodReturnValueHandler, BeanPostProcessor {


    List<ResponseBodyAdvice<Object>> advices = List.empty();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {

        Object obj = Reflect.on(returnType).get("returnValue");
        if (obj instanceof ResponseEntity) {
            return JsonConfig.get().isDefined();
        } else {
            if (JsonConfig.get().isDefined()) {
                //     Object view = Reflect.on(obj).get("view");
                System.err.println("json render exception: " + returnType + ", " + JsonConfig.get().get().getList());
            }
            return false;
        }

    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        for (ResponseBodyAdvice<Object> ad : advices) {
            if (ad.supports(returnType, null)) {
                returnValue = ad.beforeBodyWrite(returnValue, returnType, MediaType.APPLICATION_JSON_UTF8, null,
                        new ServletServerHttpRequest(webRequest.getNativeRequest(HttpServletRequest.class)),
                        new ServletServerHttpResponse(webRequest.getNativeResponse(HttpServletResponse.class)));
            }
        }

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();

        JsonConfig
                .get()
                .map(JsonConfig::getList)
                .getOrElse(List.empty())
                .forEach(jsonSerializer::filter);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (returnValue.getClass().equals(ResponseEntity.class)) {
            returnValue = Reflect.on(returnValue).get("body");
        }

        String json = jsonSerializer.toJson(returnValue);
        response.getWriter().write(json);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof ResponseBodyAdvice) {
            advices = advices.append((ResponseBodyAdvice<Object>) bean);
        } else if (bean instanceof RequestMappingHandlerAdapter) {
            System.out.println(bean.getClass() + "  " + beanName);
            List<HandlerMethodReturnValueHandler> handlers = List.ofAll(((RequestMappingHandlerAdapter) bean).getReturnValueHandlers());
            JsonReturnHandler jsonHandler = null;
            for (int i = 0; i < handlers.size(); i++) {
                HandlerMethodReturnValueHandler handler = handlers.get(i);
                if (handler instanceof JsonReturnHandler) {
                    jsonHandler = (JsonReturnHandler) handler;
                    break;
                }
            }
            if (jsonHandler != null) {
                handlers = handlers.remove(jsonHandler);
                handlers = handlers.prepend(jsonHandler);
                ((RequestMappingHandlerAdapter) bean).setReturnValueHandlers(handlers.toJavaList()); // change the jsonhandler sort
            }
        }
        return bean;
    }

}