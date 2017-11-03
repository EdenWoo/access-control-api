package com.cfgglobal.test.dao;

import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.Role;
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends BaseDao<Role, Long> {

    Option<Role> findByName(String roleName);
}