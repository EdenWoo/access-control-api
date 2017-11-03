package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.JpaBeanUtil;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.service.PermissionService;
import com.cfgglobal.test.web.base.BaseController;
import io.vavr.collection.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/v1/permission")
@Slf4j
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity page(Pageable pageable, HttpServletRequest request) {
        Page<Permission> page = permissionService.findBySecurity(request.getMethod(), request.getRequestURI(), HashMap.ofAll(request.getParameterMap()), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("{id}")
    public ResponseEntity<Permission> get(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(permissionService.findOneBySecurity(id, request.getMethod(), request.getRequestURI()));
    }

    @PostMapping
    public ResponseEntity<Permission> save(@RequestBody Permission permission, HttpServletRequest request) {
        return ResponseEntity.ok(permissionService.saveBySecurity(permission, request.getMethod(), request.getRequestURI()));
    }

    @PutMapping("{id}")
    public ResponseEntity save(@PathVariable Long id, @RequestBody Permission permission, HttpServletRequest request) {
        Permission oldPermission = permissionService.findOne(id);
        JpaBeanUtil.copyNonNullProperties(permission, oldPermission);
        return ResponseEntity.ok(permissionService.saveBySecurity(oldPermission, request.getMethod(), request.getRequestURI()));
    }

    @DeleteMapping("delete")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        permissionService.deleteBySecurity(id, request.getMethod(), request.getRequestURI());
        return ResponseEntity.noContent().build();
    }
}