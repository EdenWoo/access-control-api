package com.github.leon.aci.dao


import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.EmailLog
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.stereotype.Repository


@Repository
interface EmailLogDao : BaseDao<EmailLog, Long>

