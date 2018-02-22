package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.Rule
import io.vavr.collection.List
import io.vavr.control.Option
import org.springframework.stereotype.Repository

@Repository
interface RuleDao : BaseDao<Rule, Long> {

    fun findByName(name: String): Option<Rule>

    fun findByType(basic: String): List<Rule>
}