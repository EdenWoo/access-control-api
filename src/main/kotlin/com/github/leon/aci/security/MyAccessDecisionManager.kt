package com.github.leon.aci.security

import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class MyAccessDecisionManager : AccessDecisionManager {

    // decide 方法是判定是否拥有权限的决策方法，
    //authentication 是释CustomUserService中循环添加到 GrantedAuthority 对象中的权限信息集合.
    //object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
    //configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，
    @Throws(AccessDeniedException::class, InsufficientAuthenticationException::class)
    override fun decide(authentication: Authentication, `object`: Any, configAttributes: Collection<ConfigAttribute>) {
        if (configAttributes.isEmpty() || authentication is AnonAuthentication) {
            return
        }
        var c: ConfigAttribute
        var needRole: String
        for (configAttribute in configAttributes) {
            c = configAttribute
            needRole = c.attribute
            for (ga in authentication.authorities) {//authentication 为在注释1 中循环添加到 GrantedAuthority 对象中的权限信息集合
                if (needRole.trim { it <= ' ' }.equals(ga.authority, ignoreCase = true)) {
                    return
                }
            }
        }
        throw AccessDeniedException("no permission")
    }

    override fun supports(attribute: ConfigAttribute): Boolean {
        return true
    }

    override fun supports(clazz: Class<*>): Boolean {
        return true
    }
}