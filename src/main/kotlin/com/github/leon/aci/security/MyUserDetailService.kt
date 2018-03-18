package com.github.leon.aci.security


import com.github.leon.aci.domain.User
import com.github.leon.aci.service.UserService
import com.github.leon.cache.CacheClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MyUserDetailService(
        @Value("\${spring.application.name}")
        val application:String,
        @Autowired
        val userService: UserService,
        @Autowired
        val cacheClient: CacheClient
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): User {
        return cacheClient.get("$application-$username") { userService.getUserWithPermissions(username) }
    }


}