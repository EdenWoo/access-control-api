package com.cfgglobal.test.service.rule.access;


import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.Permission;
import org.springframework.stereotype.Component;

@Component
public class AdminAccessRule extends AbstractAccessRule {
    @Override
    public Filter exec(Permission permission) {
        return Filter.EMPTY;
    }

    @Override
    public String getRuleName() {
        return "admin";
    }
}
