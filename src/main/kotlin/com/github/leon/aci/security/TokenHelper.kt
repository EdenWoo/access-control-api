package com.github.leon.aci.security

import com.github.leon.aci.domain.User
import com.github.leon.extentions.orElse
import com.github.leon.security.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import io.jsonwebtoken.SignatureAlgorithm.HS512

@EnableConfigurationProperties(value = [(ApplicationProperties::class)])
@Component
class TokenHelper(
        @Autowired
        val applicationProperties: ApplicationProperties,
        @Autowired
        var userDetailsService: UserDetailsService,
        @Value("\${spring.application.name}")
        val application: String
) {

    fun getUsernameFromToken(token: String): String {
        val claims = this.getClaimsFromToken(token)
        return claims.subject
    }

    fun generateToken(username: String): String {
        val user = userDetailsService.loadUserByUsername(username) as User
        val currentTimeMillis = System.currentTimeMillis()
        val currentDate = Date(currentTimeMillis)
        val expirationDate = Date(currentTimeMillis + user.expiresIn.orElse(applicationProperties.jwt.expiresIn!!) * 1000)
        return Jwts.builder()
                .setIssuer(application)
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(HS512, applicationProperties.jwt.secret)
                //  .claim("user", userDetails)
                .compact()
    }

    private fun getClaimsFromToken(token: String): Claims {

        return Jwts.parser()
                .setSigningKey(applicationProperties.jwt.secret)
                .parseClaimsJws(token)
                .body
    }


    fun getToken(request: HttpServletRequest): String? {
        /**
         * Getting the token from Cookie store
         */
        val authCookie = getCookieValueByName(request, applicationProperties.jwt.cookie)
        if (authCookie != null) {
            return authCookie.value
        }
        /**
         * Getting the token from Authentication header
         * e.g Bearer your_token
         */
        val authHeader = request.getHeader(applicationProperties.jwt.header)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else request.getParameter(applicationProperties.jwt.param)

    }

    /**
     * Find a specific HTTP cookie in a request.
     *
     * @param request The HTTP request object.
     * @param name    The cookie name to look for.
     * @return The cookie, or `null` if not found.
     */
    fun getCookieValueByName(request: HttpServletRequest, name: String): Cookie? {
        if (request.cookies == null) {
            return null
        }
        for (i in 0 until request.cookies.size) {
            if (request.cookies[i].name == name) {
                return request.cookies[i]
            }
        }
        return null
    }


}