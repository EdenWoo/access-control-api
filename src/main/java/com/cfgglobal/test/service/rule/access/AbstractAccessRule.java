package com.cfgglobal.test.service.rule.access;

import com.cfgglobal.test.service.rule.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractAccessRule implements AccessRule {
    @Autowired
    protected SecurityFilter securityFilter;
}
