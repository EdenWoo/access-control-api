package com.github.leon.aci.service.rule.access

import com.github.leon.aci.service.rule.SecurityFilter
import org.springframework.beans.factory.annotation.Autowired


abstract class AbstractAccessRule : AccessRule {
    @Autowired
    protected var securityFilter: SecurityFilter? = null
}
