package com.github.leon.aci.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.leon.aci.enums.UserType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "aci_user", uniqueConstraints = [(UniqueConstraint(name = "unique_username", columnNames = arrayOf("username")))])
@DynamicUpdate
@DynamicInsert
@Inheritance(strategy = InheritanceType.JOINED)
open class User(

        var name: String? = null,

        private var username: String = "",

        @NotNull
        @JsonIgnore
        private var password: String? = null,

        @NotNull
        var email: String? = null,

        @ManyToOne
        @JoinColumn(name = "role_id")
        var role: Role? = null,

        @ManyToOne
        @JoinColumn(name = "branch_id")
        var branch: Branch? = null,


        @ManyToOne
        @JoinColumn(name = "introducer_id")
        var introducedBy: User? = null,

        @Type(type = "yes_no")
        var verify: Boolean? = null,

        @Transient
        var grantedAuthorities: MutableList<GrantedAuthority> = mutableListOf(),

        @OneToMany(cascade = [(CascadeType.PERSIST), (CascadeType.REFRESH), (CascadeType.MERGE)], orphanRemoval = true)
        var attachments: MutableList<Attachment> = mutableListOf(),

        @Enumerated(value = EnumType.STRING)
        var userType: UserType? = null,

        var expiresIn: Long? = null,

        @Transient
        var confirmPassword: String? = null


) : BaseEntity(), UserDetails {


    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String? {
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
