package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.JpaBeanUtil;
import com.cfgglobal.test.config.json.JsonConfig;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.Role;
import com.cfgglobal.test.domain.RolePermission;
import com.cfgglobal.test.domain.Rule;
import com.cfgglobal.test.service.PermissionService;
import com.cfgglobal.test.service.RoleService;
import com.cfgglobal.test.util.querydsl.Q;
import com.cfgglobal.test.web.base.BaseController;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Path;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/v1/role")
@Slf4j
public class RoleController extends BaseController {

    public static final List<Path> IGNORE_AUDITOR = List.of(Q.baseEntity.creator,
            Q.baseEntity.createdAt,
            Q.baseEntity.updatedAt,
            Q.baseEntity.modifier
    );

    public static final JsonConfig CLEAN_ROLE = JsonConfig.start().
            exclude(Role.class, IGNORE_AUDITOR, Q.role.users)
            .exclude(RolePermission.class, IGNORE_AUDITOR)
            .exclude(Permission.class, IGNORE_AUDITOR)
            .exclude(Rule.class, IGNORE_AUDITOR);

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<Page<Role>> list(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }


    @GetMapping("{id}")
    public Role get(@PathVariable Long id) {


        CLEAN_ROLE.end();
        Role role = roleService.findOne(id);
        List<RolePermission> selectedRolePermissions = List.ofAll(role.getRolePermissions());
        List<RolePermission> map = permissionService.findAll()
                .map(permission -> {
                    RolePermission rolePermission = new RolePermission().setPermission(permission);
                    rolePermission.setRules(selectedRolePermissions
                            .filter(srp -> srp.getPermission().getId().equals(rolePermission.getPermission().getId()))
                            .toOption()
                            .map(RolePermission::getRules)
                            .getOrElse(Lists.newArrayList()));
                    return rolePermission;
                });
        role.setRolePermissions(map.asJava());
        return role;

    }

    @PostMapping
    @Transactional
    public ResponseEntity<Role> role(@RequestBody Role role) {
        roleService.save(role);
        return ResponseEntity.ok(role);
    }


    @PutMapping("{id}")
    // @Transactional
    public ResponseEntity<Role> role(@PathVariable Long id, @RequestBody Role role, HttpServletRequest request) {

        Role cleanedRole = roleService.removeEmptyRules(role);
        Role oldRole = roleService.findOne(id);
        JpaBeanUtil.copyNonNullProperties(cleanedRole, oldRole);
        cleanedRole.getRolePermissions().forEach(this::syncFromDb);
        oldRole.getRolePermissions().clear();
        oldRole.getRolePermissions().addAll(cleanedRole.getRolePermissions());
        roleService.saveBySecurity(oldRole, request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok(oldRole);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        roleService.deleteBySecurity(id, request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok().build();
    }


}