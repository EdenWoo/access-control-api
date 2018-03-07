package com.cfgglobal.test.web.base

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import com.cfgglobal.test.service.UserService

import io.vavr.control.Option
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
                if (option.isDefined) {
                    Reflect.on(baseEntity).set(field.name, option.get())
                }
            } else if (field.type.isAssignableFrom(MutableList::class.java)) {
                val list = baseEntity.toOption()
                        .flatMap { it -> Reflect.on(it).get<Any>(field.name).toOption() }
                        .map { e -> e as MutableList<out BaseEntity> }
                        .map { listOf(it) }
                        .getOrElse{emptyList()}
                        .map { obj ->
                            when {
                                Reflect.on(obj).get<Any>("id") == null -> obj
                                else -> entityManager!!.find(obj.javaClass, Option.of(obj).map { e -> Reflect.on(e).get<Any>("id") }.get())
                            }
                        }
                if (!list.isEmpty()) {
                    Reflect.on(baseEntity).set(field.name, list)
                }

            }
        }
    }

    private fun getObject(baseEntity: BaseEntity, field: Field, type: Class<*>): Option<*> {
        return Option.of(baseEntity)
                .flatMap { Option.of(Reflect.on(baseEntity).get<Any>(field.name)) }
                .flatMap { Option.of(Reflect.on(it).get<Any>("id")) }
                .map { entityManager!!.find(type, it) }
    }

    protected fun getUser(baseEntity: BaseEntity): User {
        val userTry = arrow.data.Try { Reflect.on(baseEntity).get<Any>("user") as User }
        return if (userTry.isSuccess()) {
            Option.of(userTry.get())
                    .map { user -> userService!!.findOne(user.id as Long) }
                    .getOrElse(loginUser)
        } else {
            loginUser
        }

    }


}
