package com.cfgglobal.test.service.rule.access;


import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.User;
import org.springframework.stereotype.Component;

import static com.cfgglobal.test.base.Filter.OPERATOR_EQ;
import static com.cfgglobal.test.base.Filter.OPERATOR_IN;

@Component
public class IbAccessRule extends AbstractAccessRule {
    @Override
    public Filter exec(Permission permission) {
        User user = securityFilter.currentUser();
        Filter filter = new Filter();
        if (permission.getEntity().equals("user")) {
            filter.addCondition("introducedBy.id", user.getId(), OPERATOR_EQ);
        } else if (permission.getEntity().equals("price-category")) {
            filter.addCondition("id", new Object[]{5, 6, 7, 9}, OPERATOR_IN);
        } else {
            filter.addCondition("user.introducedBy.id", user.getId(), OPERATOR_EQ);
        }

        return filter;
    }

    @Override
    public String getRuleName() {
        return "ib";
    }
}
