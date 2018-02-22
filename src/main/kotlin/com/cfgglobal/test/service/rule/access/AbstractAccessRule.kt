package com.cfgglobal.test.service.rule.access

import com.cfgglobal.test.service.rule.SecurityFilter
import org.springframework.beans.factory.annotation.Autowired


abstract class AbstractAccessRule : AccessRule {
    @Autowired
    protected var securityFilter: SecurityFilter? = null
}
