package com.cfgglobal.test.web.base

import arrow.core.Option
import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import com.cfgglobal.test.service.UserService
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
            fields += User::class.java.declaredFields.toList()
        }
        fields.forEach { field ->
            val type = field.type
            if (BaseEntity::class.java.isAssignableFrom(field.type)) {
                val option = getObject(baseEntity, field, type)
                if (option.isDefined()) {
                    Reflect.on(baseEntity).set(field.name, option.get())
                }
            } else if (field.type.isAssignableFrom(MutableList::class.java)) {
                val list = listOf(baseEntity)
                        .map { _ -> Reflect.on(baseEntity).get<Any>(field.name) as BaseEntity }
                        .map {
                            val id = Reflect.on(it).get<Any>("id")
                            when (id) {
                                null -> it //ID 不存在則不從數據庫同步
                                else -> entityManager!!.find(it.javaClass, id)
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
                .flatMap { Reflect.on(baseEntity).get<Any>(field.name).toOption() }
                .flatMap { Reflect.on(it).get<Any>("id").toOption() }
                .map { entityManager!!.find(type, it) }
    }

    protected fun getUser(baseEntity: BaseEntity): User {
        val userTry = arrow.data.Try { Reflect.on(baseEntity).get<Any>("user") as User }
        return if (userTry.isSuccess()) {
            Option.fromNullable(userTry.get())
                    .flatMap { userService!!.findOne(it.id as Long) }
                    .getOrElse { loginUser }
        } else {
            loginUser
        }

    }


}
