package com.github.leon.aci.security

import arrow.core.None
import arrow.core.Some
import arrow.syntax.collections.firstOption
import com.github.leon.aci.dao.PermissionDao
import com.github.leon.cache.CacheClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.SecurityConfig
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class MyInvocationSecurityMetadataSourceService(
        @Autowired
        val permissionDao: PermissionDao,
        @Autowired
        val cacheClient: CacheClient
) : FilterInvocationSecurityMetadataSource {

    // private static ThreadLocal<ConfigAttribute> authorityHolder = new ThreadLocal<ConfigAttribute>();


    /* public static ConfigAttribute getConfigAttributeDefinition() {
        return authorityHolder.get();
    }*/


    //此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    override fun getAttributes(`object`: Any): List<ConfigAttribute>? {
        val request = (`object` as FilterInvocation).httpRequest
        val permissionOpt = cacheClient["permissions", { permissionDao.findAll() }]!!
                .firstOption { (_, _, _, authUris) ->
                    authUris.split(";").any { uriPatten -> Pattern.matches(uriPatten, request.requestURI) }
                }

        return when (permissionOpt) {
            is Some -> {
                val configAttributes = ArrayList<ConfigAttribute>()
                val cfg = SecurityConfig(permissionOpt.get().authKey)
                configAttributes.add(cfg)
                //  authorityHolder.set(configAttributes.get(0));
                configAttributes
            }
            None -> null
        }


    }

    override fun getAllConfigAttributes(): Collection<ConfigAttribute>? {
        return null
    }

    override fun supports(clazz: Class<*>): Boolean {
        return true
    }
}