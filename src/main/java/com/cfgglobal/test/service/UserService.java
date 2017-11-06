package com.cfgglobal.test.service;

import com.cfgglobal.test.dao.UserDao;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.RolePermission;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.service.base.BaseService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;


@Service
public class UserService extends BaseService<User, Long> {
    @Autowired
    private UserDao userDao;

    public User getUserWithPermissions(String username) {
        User user = userDao.findByUsername(username).getOrElseThrow((() -> new AccessDeniedException("invalid user information or user is not verified: " + username)));
        java.util.List<Permission> permissions = user.getRole().getRolePermissions().stream().map(RolePermission::getPermission).collect(Collectors.toList());
        java.util.List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Permission permission : permissions) {
            if (permission != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getAuthKey());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        user.setGrantedAuthorities(grantedAuthorities);
        return user;
    }

    public List<String> getEmails(User user) {
        return Option
                .of(user)
                .flatMap(e -> Option.of(e.getEmail()))
                .map(e -> List.of(e.split(",")))
                .getOrElse(List.empty());
    }
}
