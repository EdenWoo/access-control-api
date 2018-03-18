package com.github.leon.aci.security.handlers


import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.github.leon.aci.domain.User
import com.github.leon.aci.security.TokenHelper
import com.github.leon.aci.security.UserTokenState
import com.github.leon.aci.service.UserService
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
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableConfigurationProperties(value = [(ApplicationProperties::class)])
@Component
class AuthenticationSuccessHandler(
        @Value("\${spring.application.name}")
        var application: String,
        @Autowired
        var tokenHelper: TokenHelper,
        @Autowired
        var objectMapper: ObjectMapper,

        @Autowired
        var applicationProperties: ApplicationProperties,

        @Autowired
        var userService: UserService,

        @Autowired
        var cacheClient: CacheClient

) : SimpleUrlAuthenticationSuccessHandler() {


    @Transactional
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        clearAuthenticationAttributes(request)
        val user = authentication.principal as User

        cacheClient.set("$application-${user.username}", userService.getUserWithPermissions(user.username))

        val jws = tokenHelper.generateToken(user.username)

        val jwt = applicationProperties.jwt!!
        val authCookie = Cookie(jwt.cookie, jws)
        authCookie.path = "/"
        authCookie.isHttpOnly = true
        authCookie.maxAge = jwt.expiresIn!!.toInt()

        val userCookie = Cookie(applicationProperties.userCookie, user.username)
        userCookie.path = "/"
        userCookie.maxAge = jwt.expiresIn!!.toInt()

        response.addCookie(authCookie)
        response.addCookie(userCookie)

        val userTokenState = UserTokenState(
                access_token = jws,
                expires_in = jwt.expiresIn,
                type = user.userType.toOption().map { it.name }.getOrElse { "" }
        )


        val jwtResponse = objectMapper.writeValueAsString(userTokenState)
        response.contentType = "application/json"
        response.writer.write(jwtResponse)
    }
}
