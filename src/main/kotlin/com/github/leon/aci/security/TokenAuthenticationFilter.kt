package com.github.leon.aci.security

import arrow.core.getOrElse
import arrow.data.Failure
import arrow.data.Success
import arrow.data.Try
import arrow.syntax.option.toOption
import com.cfgglobal.test.security.TokenHelper
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.leon.aci.config.ActionReportProperties
import com.github.leon.aci.config.jpa.SecurityAuditor
import com.github.leon.aci.domain.VisitRecord
import com.github.leon.aci.exceptions.ApiResp
import com.github.leon.aci.service.VisitRecordService
import com.github.leon.aci.service.VisitRecordService.Companion.THRESHOLD

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
import java.util.*
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
        when {
            skipPathRequest(request, pathsToSkip) -> {
                SecurityContextHolder.getContext().authentication = AnonAuthentication()
                chain.doFilter(wrapRequest, response)
            }
            else -> {
                val usernameTry = tokenHelper.getUsernameFromToken(authToken)
                when (usernameTry) {
                    is Success -> {
                        val userDetails = userDetailsService.loadUserByUsername(usernameTry.value)
                        val authentication = TokenBasedAuthentication(userDetails)
                        authentication.token = authToken
                        SecurityContextHolder.getContext().authentication = authentication
                        chain.doFilter(wrapRequest, response)
                    }
                    is Failure -> {
                        loginExpired(request, response, usernameTry.exception.message!!)
                    }
                }
            }
        }
        if (actionReportProperties.isFirewall) {
            if (visitRecordService.hasTooManyRequest(Optional.ofNullable(securityAuditor.currentAuditor), getClientIp(request))) {
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
            Try { visitRecordService.save(visitRecord) }.onFailure {
                log.error("Visit record saving fail!", it)
            }
        }

    }

    private fun loginExpired(request: HttpServletRequest, response: HttpServletResponse, message: String) {
        logger.warn(request.method + request.requestURI)
        val apiResp = ApiResp()
        apiResp.message = message
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

        private val ROOT_MATCHER = "/"
        private val FAVICON_MATCHER = "/favicon.ico"
        private val HTML_MATCHER = "/**/*.html"
        private val CSS_MATCHER = "/**/*.css"
        private val JS_MATCHER = "/**/*.js"
        private val IMG_MATCHER = "/images/*"
        private val LOGIN_MATCHER = "/login"
        private val LOGOUT_MATCHER = "/logout"

        private fun getClientIp(request: HttpServletRequest): String {
            return request.getHeader("X-Forwarded-For").toOption().getOrElse { request.remoteAddr }
        }
    }
}