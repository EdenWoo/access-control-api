package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.Rule
import org.springframework.stereotype.Repository

@Repository
interface RuleDao : BaseDao<Rule, Long> {

    fun findByName(name: String): Rule

    fun findByType(basic: String): List<Rule>
}