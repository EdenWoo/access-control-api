package com.github.leon.aci.web.base

import arrow.core.Option
import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.domain.User
import com.github.leon.aci.service.UserService

import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import java.lang.reflect.Field
import javax.persistence.EntityManager

open class BaseController {

    @Autowired
    private val entityManager: EntityManager? = null
    @Autowired
    private val userService: UserService? = null

    protected val loginUser: User
        get() = SecurityContextHolder.getContext().authentication.principal as User

    protected fun syncFromDb(baseEntity: BaseEntity) {
        var fields = baseEntity.javaClass.declaredFields.toList()
        if (baseEntity.javaClass.superclass == User::class.java) {
            fields += listOf(*User::class.java.declaredFields)
        }
        fields.forEach { field ->
            val type = field.type
            if (BaseEntity::class.java.isAssignableFrom(field.type)) {
                val option = getObject(baseEntity, field, type)
                if (option.isDefined()) {
                    Reflect.on(baseEntity).set(field.name, option.get())
                }
            } else if (field.type.isAssignableFrom(MutableList::class.java)) {
                val list = baseEntity.toOption()
                        .flatMap { it -> Reflect.on(it).get<Any>(field.name).toOption() }
                        .map { e -> e as MutableList<out BaseEntity> }
                        .getOrElse{ listOf<BaseEntity>()}
                        .map { obj ->
                            val id = Reflect.on(obj).get<Any>("id")
                            when (id) {
                                null -> obj
                                else -> entityManager!!.find(obj::class.java, id)
                            }
                        }
                if (!list.isEmpty()) {
                    Reflect.on(baseEntity).set(field.name, list)
                }

            }
        }
    }

    private fun getObject(baseEntity: BaseEntity, field: Field, type: Class<*>): Option<*> {
        return baseEntity.toOption()
                .flatMap { Reflect.on(baseEntity).get<Any>(field.name).toOption()}
                .flatMap { Reflect.on(it).get<Any>("id").toOption()}
                .map { entityManager!!.find(type, it) }
    }

    protected fun getUser(baseEntity: BaseEntity): User {
        val userTry = arrow.data.Try { Reflect.on(baseEntity).get<Any>("user") as User }
        return if (userTry.isSuccess()) {
            userTry.get().toOption()
                    .map { user -> userService!!.findOne(user.id as Long) }
                    .getOrElse{loginUser}
        } else {
            loginUser
        }

    }


}
