package com.cfgglobal.test.domain

import com.cfgglobal.test.enums.UserType
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [(UniqueConstraint(name = "unique_username", columnNames = arrayOf("username")))])
@DynamicUpdate
@DynamicInsert
@Inheritance(strategy = InheritanceType.JOINED)
open class User(

        open var name: String? = null,

        private var username: String = "",

        @NotNull
        @JsonIgnore
        private var password: String = "",

        @NotNull
        var email: String? = null,
        @ManyToOne
        @JoinColumn(name = "role_id")
        open var role: Role? = null,

        @ManyToOne
        @JoinColumn(name = "branch_id")
        open var branch: Branch? = null,


        @ManyToOne
        @JoinColumn(name = "introducer_id")
        open var introducedBy: User? = null,

        @Type(type = "yes_no")
        open var verify: Boolean? = null,

        @Transient
        open var grantedAuthorities: MutableList<GrantedAuthority> = mutableListOf(),

        @OneToMany(cascade = [(CascadeType.PERSIST), (CascadeType.REFRESH), (CascadeType.MERGE)], orphanRemoval = true)
        open var attachments: MutableList<Attachment> = mutableListOf(),

        @Enumerated(value = EnumType.STRING)
        open var userType: UserType? = null

) : BaseEntity(), UserDetails {


    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password

    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return grantedAuthorities
    }

    override fun isEnabled(): Boolean {
        return true
    }


    override fun isCredentialsNonExpired(): Boolean {
        return true
    }


    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}
