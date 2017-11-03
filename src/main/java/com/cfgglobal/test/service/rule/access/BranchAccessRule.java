package com.cfgglobal.test.service.rule.access;


import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.User;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class BranchAccessRule extends AbstractAccessRule {
    @Override
    public Filter exec(Permission permission) {
        User user = securityFilter.currentUser();
        Filter orgFilter = new Filter();
        Long orgId = user.getBranch().getId();
        orgFilter.addCondition("creator.branch.id", orgId, Filter.OPERATOR_EQ);
        return orgFilter;
    }

    @Override
    public String getRuleName() {
        return "branch";
    }
}
