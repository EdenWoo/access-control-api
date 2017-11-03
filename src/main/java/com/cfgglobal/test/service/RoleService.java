package com.cfgglobal.test.service;

import com.cfgglobal.test.domain.Role;
import com.cfgglobal.test.domain.RolePermission;
import com.cfgglobal.test.service.base.BaseService;
import io.vavr.collection.List;
import org.springframework.stereotype.Service;


@Service
public class RoleService extends BaseService<Role, Long> {

    public Role removeEmptyRules(Role role) {
        java.util.List<RolePermission> rolePermissions = role.getRolePermissions();
        return role.setRolePermissions(List.ofAll(rolePermissions)
                .filter(e -> e.getRules() != null && !e.getRules().isEmpty())
                .asJava());
    }
}
