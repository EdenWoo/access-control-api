package com.cfgglobal.test.service.rule;

import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.*;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class SecurityFilterImpl implements SecurityFilter {


    @Autowired
    private RuleService ruleService;

    @Override
    public User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    @Override
    public List<Filter> query(String method, String requestURI) {
        User user = currentUser();
        Role role = user.getRole();
        java.util.List<RolePermission> rolePermissions = role.getRolePermissions();

        List<Permission> permissions = List.ofAll(rolePermissions).map(RolePermission::getPermission);
        Permission permission = permissions
                .filter(resource -> Stream.of(resource.getAuthUris().split(";"))
                        .anyMatch(uriPatten -> Pattern.matches(uriPatten, requestURI)))
                .headOption()
                .getOrElseThrow(() -> new AccessDeniedException(MessageFormat.format("No permission {0} {1}", method, requestURI)));

        java.util.List<Rule> rules = List.ofAll(rolePermissions).filter(rp -> rp.getPermission().getId().equals(permission.getId())).head().getRules();
        return List.ofAll(rules).map(rule -> ruleService.findAccessRules(rule.getName()).exec(permission));

    }
}
