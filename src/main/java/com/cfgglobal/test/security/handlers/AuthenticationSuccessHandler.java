package com.cfgglobal.test.security.handlers;


import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.security.TokenHelper;
import com.cfgglobal.test.security.UserTokenState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableConfigurationProperties(value = {ApplicationProperties.class})
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    TokenHelper tokenHelper;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationProperties applicationProperties;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        User user = (User) authentication.getPrincipal();

        String jws = tokenHelper.generateToken(user.getUsername());

        // Create token auth Cookie
        Cookie authCookie = new Cookie(applicationProperties.getJwt().getCookie(), (jws));
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(applicationProperties.getJwt().getExpiresIn().intValue());
        // Create flag Cookie
        Cookie userCookie = new Cookie(applicationProperties.getUserCookie(), (user.getUsername()));
        userCookie.setPath("/");
        userCookie.setMaxAge(applicationProperties.getJwt().getExpiresIn().intValue());
        // Add cookie to response
        response.addCookie(authCookie);
        response.addCookie(userCookie);
        // JWT is also in the response
        UserTokenState userTokenState = new UserTokenState();
        userTokenState.setAccess_token(jws);
        userTokenState.setExpires_in(applicationProperties.getJwt().getExpiresIn());
        userTokenState.setType(user.getUserType().name());

        String jwtResponse = objectMapper.writeValueAsString(userTokenState);
        response.setContentType("application/json");
        response.getWriter().write(jwtResponse);
    }
}
