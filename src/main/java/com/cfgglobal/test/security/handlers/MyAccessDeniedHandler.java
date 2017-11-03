package com.cfgglobal.test.security.handlers;

import com.cfgglobal.test.base.ApiResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ApiResp apiResp = new ApiResp();
        apiResp.setError("AccessDenied: " + request.getRequestURI());
        String msg = objectMapper.writeValueAsString(apiResp);
        response.setStatus(403);
        response.getWriter().write(msg);

    }
}
