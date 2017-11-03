package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.ClassSearcher;
import com.cfgglobal.test.dao.PermissionDao;
import com.cfgglobal.test.dao.RoleDao;
import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.Role;
import com.cfgglobal.test.service.GeneratorService;
import com.cfgglobal.test.service.PermissionService;
import com.cfgglobal.test.service.RoleService;
import com.google.common.collect.Lists;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/sys")
public class SystemController {

    @Autowired
    GeneratorService userService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    RoleDao roleDao;


    @GetMapping("/entity")
    public List<String> entity() {
        //permissionService
        return List.ofAll(ClassSearcher.of(BaseEntity.class).search()).map(e -> e.getSimpleName());

    }

    @RequestMapping(value = "/permission", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public java.util.List<Permission> init(String entityName) {
        java.util.List<Permission> permissions = Lists.newArrayList();
        Consumer<Object> entityToPermission = e -> {
            BaseEntity entity = (BaseEntity) e;
            String name = e.getClass().getSimpleName();
            permissionDao.delete(permissionDao.findByEntity(name));
            permissions.addAll(userService.genPermission(entity).asJava());
            permissionService.save(permissions);
        };
        if (entityName != null) {
            String name = BaseEntity.class.getPackage().getName() + "." + entityName;
            System.out.println("entity:  " + name);
            Reflect reflect = Reflect.on(name);
            entityToPermission.accept(reflect.create().get());
        } else {
            io.vavr.collection.List.ofAll(ClassSearcher.of(BaseEntity.class).search())
                    .map(e -> {
                        Reflect reflect = Reflect.on(e.getName());
                        return reflect.create().get();
                    })
                    .forEach(entityToPermission);

        }
        return permissions;
    }

    @GetMapping("/assign")
    @Transactional
    public ResponseEntity<Role> assign(String roleName, String rule) {
        List<Permission> permissions = permissionService.findAll();
        Role role = roleDao.findByName(roleName).getOrElseThrow(() -> new IllegalArgumentException(roleName));
        role.getRolePermissions().clear();
        role.getRolePermissions().addAll(userService.assignPermission(permissions, rule).asJava());
        roleService.save(role);
        //TODO
        //SharedConfig.CLEAN_ROLE.end();
        return ResponseEntity.ok(role);
    }
}
