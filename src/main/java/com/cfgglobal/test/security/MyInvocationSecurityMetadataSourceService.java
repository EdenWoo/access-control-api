package com.cfgglobal.test.security;

import com.cfgglobal.test.dao.PermissionDao;
import com.cfgglobal.test.domain.Permission;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    // private static ThreadLocal<ConfigAttribute> authorityHolder = new ThreadLocal<ConfigAttribute>();


    @Autowired
    private PermissionDao permissionDao;

    private List<Permission> permissions;

    @PostConstruct
    private void loadPermissions() {
        permissions = permissionDao.findAll();
    }
   /* public static ConfigAttribute getConfigAttributeDefinition() {
        return authorityHolder.get();
    }*/


    //此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public List<ConfigAttribute> getAttributes(Object object) {
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        Option<Permission> permissionOpt = io.vavr.collection.List.ofAll(permissions)
                .filter(resource -> Stream.of(resource.getAuthUris().split(";"))
                        .anyMatch(uriPatten -> Pattern.matches(uriPatten, request.getRequestURI())))
                .headOption();

        if (permissionOpt.isDefined()) {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            SecurityConfig cfg = new SecurityConfig(permissionOpt.get().getAuthKey());
            configAttributes.add(cfg);
            //  authorityHolder.set(configAttributes.get(0));
            return configAttributes;
        } else {
            return null;
        }


    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}