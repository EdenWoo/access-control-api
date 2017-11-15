package com.cfgglobal.test.security;


import com.cfgglobal.test.cache.CacheClient;
import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    CacheClient cacheClient;

    @Transactional
    public User loadUserByUsername(String username) {
        return cacheClient.get(applicationProperties.getUserClass() + "-" + username, () -> userService.getUserWithPermissions(username));
    }


}