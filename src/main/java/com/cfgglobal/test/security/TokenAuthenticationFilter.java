package com.cfgglobal.test.security;

import com.cfgglobal.test.base.ApiResp;
import com.cfgglobal.test.config.app.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String ROOT_MATCHER = "/";
    private static final String FAVICON_MATCHER = "/favicon.ico";
    private static final String HTML_MATCHER = "/**/*.html";
    private static final String CSS_MATCHER = "/**/*.css";
    private static final String JS_MATCHER = "/**/*.js";
    private static final String IMG_MATCHER = "/images/*";
    private static final String LOGIN_MATCHER = "/login";
    private static final String LOGOUT_MATCHER = "/logout";
    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationProperties applicationProperties;
    @Autowired
    UserDetailsService userDetailsService;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {


        List<String> pathsToSkip = Option.of(applicationProperties.getJwt().getAnonymousUrls())
                .map(url -> url.split(","))
                .map(List::of)
                .getOrElse(List.empty());

        pathsToSkip = pathsToSkip.appendAll(List.of(
                ROOT_MATCHER,
                HTML_MATCHER,
                FAVICON_MATCHER,
                CSS_MATCHER,
                JS_MATCHER,
                IMG_MATCHER,
                LOGIN_MATCHER,
                LOGOUT_MATCHER,
                "/v1/payment/*",
                "/v1/code/*",
                "/sys/*",
                "/files/*",
                "/images/mail/*",
                "/v1/transaction/*/receipt", //for email
                "/v1/payment/*",
                "/less/*",
                "/less/material/*",
                "/images/payment/*"

        ));

        String authToken = tokenHelper.getToken(request);
        if (skipPathRequest(request, pathsToSkip)) {
            SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication());
            chain.doFilter(request, response);
        } else if (authToken != null && !authToken.equals("null") && !authToken.equals("undefined")) {
            String username = tokenHelper.getUsernameFromToken(authToken);
            if (username == null) {
                log.error("username is null , token {}", authToken);
                loginExpired(request, response);
            } else {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }
        } else {
            loginExpired(request, response);
        }


    }

    private void loginExpired(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.warn(request.getMethod() + request.getRequestURI());
        ApiResp apiResp = new ApiResp();
        apiResp.setError("login expired");
        String msg = objectMapper.writeValueAsString(apiResp);
        response.setStatus(403);
        response.getWriter().write(msg);
    }

    private boolean skipPathRequest(HttpServletRequest request, List<String> pathsToSkip) {
        List<RequestMatcher> m = pathsToSkip.map(AntPathRequestMatcher::new);
        OrRequestMatcher matchers = new OrRequestMatcher(m.toJavaList());
        return matchers.matches(request);
    }

}