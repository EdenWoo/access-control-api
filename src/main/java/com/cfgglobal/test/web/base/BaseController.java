package com.cfgglobal.test.web.base;

import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.service.UserService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class BaseController {


    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserService userService;

    protected void syncFromDb(BaseEntity baseEntity) {
        List<Field> fields = List.of(baseEntity.getClass().getDeclaredFields());
        if (baseEntity.getClass().getSuperclass() == User.class) {
            fields = fields.appendAll(List.of(User.class.getDeclaredFields()));
        }
        fields.forEach(field -> {
            Class<?> type = field.getType();
            if (BaseEntity.class.isAssignableFrom(field.getType())) {
                Option option = getObject(baseEntity, field, type);
                if (option.isDefined()) {
                    Reflect.on(baseEntity).set(field.getName(), option.get());
                }
            } else if (field.getType().isAssignableFrom(java.util.List.class)) {
                List<? extends BaseEntity> list = List.ofAll(Option.of(baseEntity)
                        .flatMap(e -> Option.of(Reflect.on(baseEntity).get(field.getName())))
                        .map(e -> (java.util.List<? extends BaseEntity>) e)
                        .map(List::ofAll)
                        .getOrElse(List.empty())
                        .map(obj -> {
                            BaseEntity entity;
                            if (Reflect.on(obj).get("id") == null) {
                                entity = obj;
                            } else {
                                entity = entityManager.find(obj.getClass(), Option.of(obj).map(e -> Reflect.on(e).get("id")).get());
                            }
                            return entity;
                        }));
                if (!list.isEmpty()) {
                    Reflect.on(baseEntity).set(field.getName(), list.asJava());
                }

            }
        });
    }

    private Option getObject(BaseEntity baseEntity, Field field, Class<?> type) {
        return Option.of(baseEntity)
                .flatMap(e -> Option.of(Reflect.on(baseEntity).get(field.getName())))
                .flatMap(e -> Option.of(Reflect.on(e).get("id")))
                .map(e -> entityManager.find(type, e));
    }

    protected User getUser(BaseEntity baseEntity) {
        Try<User> userTry = Try.of(() -> (User) Reflect.on(baseEntity).get("user"));
        if (userTry.isSuccess()) {
            return Option.of(userTry.get())
                    .map(user -> userService.findOne(user.getId()))
                    .getOrElse(getLoginUser());
        } else {
            return getLoginUser();
        }

    }

    protected User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    protected void handle(String ids, Consumer<Long> longConsumer) {
        Option.of(ids).map(e -> e.split(","))
                .map(List::of)
                .getOrElse(List.empty())
                .map(Long::parseLong)
                .forEach(longConsumer);
    }

}
