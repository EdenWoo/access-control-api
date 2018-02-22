package com.cfgglobal.test.security.handlers


import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.domain.User
import com.cfgglobal.test.security.TokenHelper
import com.cfgglobal.test.security.UserTokenState
import com.cfgglobal.test.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.leon.cache.CacheClient
import com.github.leon.security.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableConfigurationProperties(value = [(ApplicationProperties::class)])
@Component
class AuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    @Value("spring.profiles.active")
    internal var profile: String? = null
    @Autowired
    internal var tokenHelper: TokenHelper? = null
    @Autowired
    internal var objectMapper: ObjectMapper? = null

    @Autowired
    internal var applicationProperties: ApplicationProperties? = null


    @Autowired
    internal var userService: UserService? = null

    @Autowired
    internal var cacheClient: CacheClient? = null

    @Transactional
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        clearAuthenticationAttributes(request)
        val user = authentication.principal as User

        if ("local" != profile) {
            cacheClient!!.set(applicationProperties!!.userClass + "-" + user.username, userService!!.getUserWithPermissions(user.username))
        }

        val jws = tokenHelper!!.generateToken(user.username)

        val jwt = applicationProperties!!.jwt!!
        val authCookie = Cookie(jwt.cookie, jws)
        authCookie.path = "/"
        authCookie.isHttpOnly = true
        authCookie.maxAge = jwt.expiresIn!!.toInt()

        val userCookie = Cookie(applicationProperties!!.userCookie, user.username)
        userCookie.path = "/"
        userCookie.maxAge = jwt.expiresIn!!.toInt()

        response.addCookie(authCookie)
        response.addCookie(userCookie)

        val userTokenState = UserTokenState(
                access_token = jws,
                expires_in = jwt.expiresIn,
                type = user.userType.toOption().map { it.name }.getOrElse { "" }
        )


        val jwtResponse = objectMapper!!.writeValueAsString(userTokenState)
        response.contentType = "application/json"
        response.writer.write(jwtResponse)
    }
}
