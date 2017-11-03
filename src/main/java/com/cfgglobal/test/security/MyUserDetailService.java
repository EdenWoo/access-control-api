package com.cfgglobal.test.security;


import com.cfgglobal.test.cache.CacheClient;
import com.cfgglobal.test.dao.UserDao;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.RolePermission;
import com.cfgglobal.test.domain.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Autowired
    CacheClient cacheClient;

    @Transactional
    public User loadUserByUsername(String username) {
        return getUser(username);
        //return cacheClient.get(LOGIN_USER + username, () -> getUser(username));
    }

    private User getUser(String username) {
        User user = userDao.findByUsername(username).filter(User::getVerify).getOrElseThrow((() -> new AccessDeniedException("invalid user information or user is not verified: " + username)));
        List<Permission> permissions = user.getRole().getRolePermissions().stream().map(RolePermission::getPermission).collect(Collectors.toList());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Permission permission : permissions) {
            if (permission != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getAuthKey());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        user.setGrantedAuthorities(grantedAuthorities);
        return user;
    }

}