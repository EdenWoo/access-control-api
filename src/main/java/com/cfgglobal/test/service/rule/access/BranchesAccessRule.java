package com.cfgglobal.test.service.rule.access;


import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.dao.BranchDao;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.User;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class BranchesAccessRule extends AbstractAccessRule {
    @Autowired
    private BranchDao branchDao;

    @Override
    public Filter exec(Permission permission) {
        User user = securityFilter.currentUser();
        Filter orgFilter = new Filter();
        Long orgId = user.getBranch().getId();
        List<Long> ids = branchDao.findSubOrgIds(user.getBranch().getId());
        ids = ids.append(orgId);
        orgFilter.addCondition("creator.branch.id", ids.toJavaArray(), Filter.OPERATOR_IN);
        return orgFilter;
    }

    @Override
    public String getRuleName() {
        return "branches";
    }
}
