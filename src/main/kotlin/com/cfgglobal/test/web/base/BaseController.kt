package com.cfgglobal.test.web.base

import com.cfgglobal.test.domain.BaseEntity
import com.cfgglobal.test.domain.User
import com.cfgglobal.test.service.UserService
import io.vavr.collection.List
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
        var fields = List.of(*baseEntity.javaClass.declaredFields)
        if (baseEntity.javaClass.superclass == User::class.java) {
            fields = fields.appendAll(List.of(*User::class.java.declaredFields))
        }
        fields.forEach { field ->
            val type = field.type
            if (BaseEntity::class.java.isAssignableFrom(field.type)) {
                val option = getObject(baseEntity, field, type)
                if (option.isDefined) {
                    Reflect.on(baseEntity).set(field.name, option.get())
                }
            } else if (field.type.isAssignableFrom(MutableList::class.java)) {
                val list = List.ofAll(Option.of(baseEntity)
                        .flatMap { e -> Option.of(Reflect.on(baseEntity).get<Any>(field.name)) }
                        .map { e -> e as MutableList<out BaseEntity> }
                        .map({ List.ofAll(it) })
                        .getOrElse(List.empty())
                        .map { obj ->
                            val entity: BaseEntity
                            if (Reflect.on(obj).get<Any>("id") == null) {
                                entity = obj
                            } else {
                                entity = entityManager!!.find(obj.javaClass, Option.of(obj).map { e -> Reflect.on(e).get<Any>("id") }.get())
                            }
                            entity
                        })
                if (!list.isEmpty) {
                    Reflect.on(baseEntity).set(field.name, list.asJava())
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
