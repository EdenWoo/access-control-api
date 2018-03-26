package com.github.leon.aci.service

import com.github.leon.aci.dao.EmailLogDao
import com.github.leon.aci.domain.EmailLog
import com.github.leon.aci.service.base.BaseService
import com.github.leon.aci.vo.Condition
import com.github.leon.aci.vo.Filter
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmailLogService : BaseService<EmailLog, Long>() {

    @Autowired
    private val emailLogDao: EmailLogDao? = null


    fun findForSend(customEmail: EmailLog): List<EmailLog> {
        val cond1 = Condition(
                fieldName = "status",
                value = customEmail.status,
                operator = Filter.OPERATOR_EQ
        )
        val cond2 = Condition(
                fieldName = "times",
                value = customEmail.times,
                operator = Filter.OPERATOR_LESS_EQ
        )
        val conds = listOf(
                cond1,
                cond2

        ).filter { it.value != null }
        val filter = Filter(conditions = conds)

        return findByFilter(filter)
    }


    fun update(customEmailLog: EmailLog) {
        emailLogDao!!.save(customEmailLog)
    }


    operator fun get(id: String): EmailLog {
        return emailLogDao!!.findOne(NumberUtils.createLong(id)!!)
    }

    operator fun get(id: Long?): EmailLog {
        return emailLogDao!!.findOne(id!!)
    }
}
