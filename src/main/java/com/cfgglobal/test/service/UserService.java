package com.cfgglobal.test.service;

import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.service.base.BaseService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;


@Service
public class UserService extends BaseService<User, Long> {

    public List<String> getEmails(User user) {
        return Option
                .of(user)
                .flatMap(e -> Option.of(e.getEmail()))
                .map(e -> List.of(e.split(",")))
                .getOrElse(List.empty());
    }
}
