package com.cfgglobal.test.security

import com.github.leon.security.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@EnableConfigurationProperties(value = [(ApplicationProperties::class)])
@Component
class TokenHelper(
        @Autowired
        val applicationProperties: ApplicationProperties
) {
    @Autowired
    var userDetailsService: UserDetailsService? = null
    @Value("\${spring.application.name}")
    private val APP_NAME: String? = null
    private val SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()

    fun getUsernameFromToken(token: String): String? {
        var username: String?
        try {
            val claims = this.getClaimsFromToken(token)
            username = claims!!.subject
        } catch (e: Exception) {
            username = null
        }

        return username
    }

    fun generateToken(username: String): String {
        // User userDetails = (User) userDetailsService.loadUserByUsername(username);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, applicationProperties.jwt.secret)
                //  .claim("user", userDetails)
                .compact()
    }

    private fun getClaimsFromToken(token: String): Claims? {
        var claims: Claims?
        try {
            claims = Jwts.parser()
                    .setSigningKey(applicationProperties.jwt.secret)
                    .parseClaimsJws(token)
                    .body
        } catch (e: Exception) {
            claims = null
        }

        return claims
    }

    internal fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, applicationProperties.jwt.secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String): Boolean? {
        try {
            val expirationDate = getClaimsFromToken(token)!!.expiration
            val username = getUsernameFromToken(token)
            val userDetails = userDetailsService!!.loadUserByUsername(username)
            return expirationDate.compareTo(generateCurrentDate()) > 0
        } catch (e: Exception) {
            return false
        }

    }

    fun refreshToken(token: String): String? {
        var refreshedToken: String?
        try {
            val claims = getClaimsFromToken(token)
            claims!!.issuedAt = generateCurrentDate()
            refreshedToken = generateToken(claims)
        } catch (e: Exception) {
            refreshedToken = null
        }

        return refreshedToken
    }

    private fun generateCurrentDate(): Date {
        return Date(currentTimeMillis)
    }

    private fun generateExpirationDate(): Date {

        return Date(currentTimeMillis + applicationProperties.jwt.expiresIn!! * 1000)
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
        val authHeader = request.getHeader(applicationProperties!!.jwt.header)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else request.getParameter(applicationProperties!!.jwt.param)

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