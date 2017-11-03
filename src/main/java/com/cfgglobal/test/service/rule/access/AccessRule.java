package com.cfgglobal.test.service.rule.access;

import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.Permission;

public interface AccessRule {

    Filter exec(Permission permission);

    String getRuleName();
}
