package com.cfgglobal.test.web.api;

import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.dao.UserDao;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.security.TokenHelper;
import com.cfgglobal.test.security.UserTokenState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class AuthenticationController {

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {

        String authToken = tokenHelper.getToken(request);

        if (authToken != null && tokenHelper.canTokenBeRefreshed(authToken)) {
            String refreshedToken = tokenHelper.refreshToken(authToken);
            Cookie authCookie = new Cookie(applicationProperties.getJwt().getCookie(), (refreshedToken));
            authCookie.setPath("/");
            authCookie.setHttpOnly(true);
            authCookie.setMaxAge(Math.toIntExact(applicationProperties.getJwt().getExpiresIn()));
            response.addCookie(authCookie);
            UserTokenState userTokenState = new UserTokenState();
            User user = userDao.findByUsername(tokenHelper.getUsernameFromToken(authToken)).get();
            userTokenState.setAccess_token(refreshedToken);
            userTokenState.setExpires_in(applicationProperties.getJwt().getExpiresIn());
            userTokenState.setType(user.getUserType().name());
            return ResponseEntity.ok(userTokenState);
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted().body(userTokenState);
        }
    }
}