package com.cfgglobal.test.dao;

import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.User;
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User, Long> {

    Option<User> findByUsername(String name);

}