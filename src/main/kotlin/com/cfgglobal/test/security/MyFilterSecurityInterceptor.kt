package com.cfgglobal.test.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.SecurityMetadataSource
import org.springframework.security.access.intercept.AbstractSecurityInterceptor
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.stereotype.Service
import java.io.IOException
import javax.servlet.*

@Service
class MyFilterSecurityInterceptor : AbstractSecurityInterceptor(), Filter {


    @Autowired
    private val securityMetadataSource: FilterInvocationSecurityMetadataSource? = null

    @Autowired
    fun setMyAccessDecisionManager(myAccessDecisionManager: MyAccessDecisionManager) {
        super.setAccessDecisionManager(myAccessDecisionManager)
    }


    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {

    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        val fi = FilterInvocation(request, response, chain)
        invoke(fi)
    }


    @Throws(IOException::class, ServletException::class)
    operator fun invoke(fi: FilterInvocation) {

        val token = super.beforeInvocation(fi)
        try {
            fi.chain.doFilter(fi.request, fi.response)
        } finally {
            super.afterInvocation(token, null)
        }
    }

    override fun destroy() {

    }

    override fun getSecureObjectClass(): Class<*> {
        return FilterInvocation::class.java
    }

    override fun obtainSecurityMetadataSource(): SecurityMetadataSource? {
        return this.securityMetadataSource
    }
}