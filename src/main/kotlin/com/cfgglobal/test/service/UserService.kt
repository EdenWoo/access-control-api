package com.cfgglobal.test.service

import arrow.core.None
import arrow.core.Some
import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.dao.UserDao
import com.cfgglobal.test.domain.User
import com.cfgglobal.test.service.base.BaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service


@Service
class UserService(
        @Autowired val userDao: UserDao
) : BaseService<User, Long>() {
    fun getUserWithPermissions(username: String): User {
        val userOpt = userDao.findByUsername(username).toOption()
                .filter { it.verify!! }
        val user = when (userOpt) {
            is Some -> userOpt.t
            None -> throw AccessDeniedException("invalid user information or user is not verified: $username")
        }
        val permissions = user.role!!.rolePermissions.map { it.permission }
        val grantedAuthorities = permissions.map { SimpleGrantedAuthority(it!!.authKey) as GrantedAuthority }.toMutableList()
        user.grantedAuthorities = grantedAuthorities
        return user
    }

    fun getEmails(user: User): List<String> {
        return user.toOption()
                .flatMap { it.email.toOption() }
                .map { it.split(",") }
                .getOrElse { (emptyList()) }
    }
}
