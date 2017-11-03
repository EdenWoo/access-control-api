package com.cfgglobal.test.dao;

import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.Permission;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends BaseDao<Permission, Long> {

    Option<Permission> findByHttpMethod(String httpMethod);

    List<Permission> findByEntity(String entity);
}