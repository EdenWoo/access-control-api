package com.cfgglobal.test.security;

import com.cfgglobal.test.config.app.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


@EnableConfigurationProperties(value = {ApplicationProperties.class})
@Component
public class TokenHelper {

    @Autowired
    ApplicationProperties applicationProperties;
    @Autowired
    UserDetailsService userDetailsService;
    @Value("${spring.application.name}")
    private String APP_NAME;
    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String generateToken(String username) {
        // User userDetails = (User) userDetailsService.loadUserByUsername(username);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, applicationProperties.getJwt().getSecret())
                //  .claim("user", userDetails)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(applicationProperties.getJwt().getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, applicationProperties.getJwt().getSecret())
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        try {
            final Date expirationDate = getClaimsFromToken(token).getExpiration();
            String username = getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return expirationDate.compareTo(generateCurrentDate()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.setIssuedAt(generateCurrentDate());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    private long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    private Date generateExpirationDate() {

        return new Date(getCurrentTimeMillis() + applicationProperties.getJwt().getExpiresIn() * 1000);
    }

    public String getToken(HttpServletRequest request) {
        /**
         *  Getting the token from Cookie store
         */
        Cookie authCookie = getCookieValueByName(request, applicationProperties.getJwt().getCookie());
        if (authCookie != null) {
            return authCookie.getValue();
        }
        /**
         *  Getting the token from Authentication header
         *  e.g Bearer your_token
         */
        String authHeader = request.getHeader(applicationProperties.getJwt().getHeader());
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        /**
         *  Getting the token from HttpRequest
         */
        String token = request.getParameter(applicationProperties.getJwt().getParam());
        if (token != null) {
            return token;
        }

        return null;
    }

    /**
     * Find a specific HTTP cookie in a request.
     *
     * @param request The HTTP request object.
     * @param name    The cookie name to look for.
     * @return The cookie, or <code>null</code> if not found.
     */
    public Cookie getCookieValueByName(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (int i = 0; i < request.getCookies().length; i++) {
            if (request.getCookies()[i].getName().equals(name)) {
                return request.getCookies()[i];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.escapeHtml4("222&"));
    }
}