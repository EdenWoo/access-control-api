package com.cfgglobal.test.web.api;

import com.cfgglobal.test.domain.RolePermission;
import com.cfgglobal.test.domain.Rule;
import com.cfgglobal.test.service.PermissionService;
import com.cfgglobal.test.service.rule.RuleService;
import com.cfgglobal.test.web.base.BaseController;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1/role-permission")
@Slf4j
public class RolePermissionController extends BaseController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<RolePermission>> list() {
        List<Rule> rules = ruleService.findAll();
        List<RolePermission> map = permissionService.findAll()
                .map(permission -> new RolePermission().setPermission(permission).setRules(rules.asJava()));
        return ResponseEntity.ok(map);

    }

}