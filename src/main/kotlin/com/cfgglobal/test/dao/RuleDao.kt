package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.Rule
import org.springframework.stereotype.Repository

@Repository
interface RuleDao : BaseDao<Rule, Long> {

    fun findByName(name: String): Rule

    fun findByType(basic: String): List<Rule>
}