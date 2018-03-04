package com.cfgglobal.test.security

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.config.ActionReportProperties
import com.cfgglobal.test.config.jpa.SecurityAuditor
import com.cfgglobal.test.domain.VisitRecord
import com.cfgglobal.test.service.VisitRecordService
import com.cfgglobal.test.service.VisitRecordService.Companion.THRESHOLD
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.leon.exceptions.ApiResp
import com.github.leon.security.ApplicationProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.time.Instant
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@EnableConfigurationProperties(value = [(ApplicationProperties::class), (ActionReportProperties::class)])
@Component
class TokenAuthenticationFilter(
        @Autowired
        val securityAuditor: SecurityAuditor,
        @Autowired
        val tokenHelper: TokenHelper,
        @Autowired
        val objectMapper: ObjectMapper,
        @Autowired
        val applicationProperties: ApplicationProperties,

        @Autowired
        val actionReportProperties: ActionReportProperties,
        @Autowired
        val userDetailsService: UserDetailsService,
        @Autowired
        val visitRecordService: VisitRecordService

) : OncePerRequestFilter() {

    val log = LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)!!
    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val start = Instant.now().epochSecond
        val wrapRequest = AuthenticationRequestWrapper(request)
        var pathsToSkip = applicationProperties.jwt.anonymousUrls.toOption()
                .map { url -> url.split(",") }
                .map { it.toList() }
                .getOrElse { emptyList() }

        pathsToSkip += listOf(
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

        )

        val authToken = tokenHelper.getToken(request)
        if (skipPathRequest(request, pathsToSkip)) {
            SecurityContextHolder.getContext().authentication = AnonAuthentication()
            chain.doFilter(wrapRequest, response)
        } else if (authToken != null && authToken != "null" && authToken != "undefined") {
            val username = tokenHelper.getUsernameFromToken(authToken)
            if (username == null) {
                log.error("username is null , token {}", authToken)
                loginExpired(request, response)
            } else {
                val userDetails = userDetailsService.loadUserByUsername(username)
                val authentication = TokenBasedAuthentication(userDetails)
                authentication.token = authToken
                SecurityContextHolder.getContext().authentication = authentication
                chain.doFilter(wrapRequest, response)
            }
        } else {
            println("URI" + request.requestURI)
            loginExpired(request, response)
        }

        if (actionReportProperties.isFirewall) {
            if (visitRecordService.hasTooManyRequest(securityAuditor.currentAuditor, getClientIp(request))) {
                val apiResp = ApiResp()
                apiResp.error = THRESHOLD.toString() + " requests allowed per min, if you need more, please contact us."
                val msg = objectMapper.writeValueAsString(apiResp)
                response.status = 429
                response.writer.write(msg)
            }
        }

        if (actionReportProperties.isVisitRecord) {
            val end = Instant.now().epochSecond
            val visitRecord = VisitRecord(
                    ip = getClientIp(request),
                    method = request.method,
                    uri = request.requestURI,
                    requestBody = wrapRequest.payload,
                    queryString = request.queryString,
                    executionTime = (end - start))
            visitRecordService.save(visitRecord)
        }

    }

    @Throws(IOException::class)
    private fun loginExpired(request: HttpServletRequest, response: HttpServletResponse) {
        logger.warn(request.method + request.requestURI)
        val apiResp = ApiResp()
        apiResp.error = "login expired"
        val msg = objectMapper.writeValueAsString(apiResp)
        response.status = 403
        response.writer.write(msg)
    }

    private fun skipPathRequest(request: HttpServletRequest, pathsToSkip: List<String>): Boolean {
        val m = pathsToSkip.map { AntPathRequestMatcher(it) }
        return OrRequestMatcher(m).matches(request)
    }

    companion object {

        private const val ROOT_MATCHER = "/"
        private const val FAVICON_MATCHER = "/favicon.ico"
        private const val HTML_MATCHER = "/**/*.html"
        private const val CSS_MATCHER = "/**/*.css"
        private const val JS_MATCHER = "/**/*.js"
        private const val IMG_MATCHER = "/images/*"
        private const val LOGIN_MATCHER = "/login"
        private const val LOGOUT_MATCHER = "/logout"

        private fun getClientIp(request: HttpServletRequest): String {
            return request.getHeader("X-Forwarded-For").toOption().getOrElse { request.remoteAddr }
        }
    }
}