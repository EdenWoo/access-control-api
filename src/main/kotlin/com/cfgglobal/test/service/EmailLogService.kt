package com.cfgglobal.test.service

import com.cfgglobal.test.dao.EmailLogDao
import com.cfgglobal.test.domain.EmailLog
import com.cfgglobal.test.service.base.BaseService
import com.cfgglobal.test.vo.Filter
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmailLogService : BaseService<EmailLog, Long>() {

    @Autowired
    private val emailLogDao: EmailLogDao? = null


    fun findForSend(customEmail: EmailLog): List<EmailLog> {
        val filter = Filter().addCondition("status", customEmail.status, Filter.OPERATOR_EQ)
                .addCondition("times", customEmail.times, Filter.OPERATOR_LESS_EQ)
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
