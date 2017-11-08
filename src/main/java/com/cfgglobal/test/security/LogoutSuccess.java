package com.cfgglobal.test.security;

import com.cfgglobal.test.cache.CacheClient;
import com.cfgglobal.test.config.app.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class LogoutSuccess implements LogoutSuccessHandler {

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CacheClient cacheClient;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        cacheClient.deleteByKey(applicationProperties.getUserClass() + "-" + authentication.getName());
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.setStatus(HttpServletResponse.SC_OK);

    }

}