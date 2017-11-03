package com.cfgglobal.test.service.rule.access;


import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.User;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class CreatorAccessRule extends AbstractAccessRule {
    @Override
    public Filter exec(Permission permission) {
        User user = securityFilter.currentUser();
        Filter orgFilter = new Filter();
        orgFilter.addCondition("creator.id", user.getId(), Filter.OPERATOR_EQ);
        return orgFilter;
    }

    @Override
    public String getRuleName() {
        return "creator";
    }
}
