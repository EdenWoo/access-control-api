package com.github.leon.email.service

import com.github.leon.aci.enums.TaskStatus
import com.github.leon.aci.service.base.BaseService
import com.github.leon.aci.vo.Condition
import com.github.leon.aci.vo.Filter
import com.github.leon.email.dao.EmailLogDao
import com.github.leon.email.domain.EmailLog
import com.github.leon.freemarker.FreemarkerBuilderUtil
import org.apache.commons.lang3.math.NumberUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class EmailLogService(
        @Autowired
        val emailLogDao: EmailLogDao,
        @Autowired
        val freemarkerBuilderUtil: FreemarkerBuilderUtil
) : BaseService<EmailLog, Long>() {

    val log = LoggerFactory.getLogger(EmailLogService::class.java)!!
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

    fun sendSystem(subject: String, sendTo: String, ftl: String, model: Map<String, Any?>) {
        try {
            val emailLog = EmailLog(
                    times = 0,
                    sendTo = sendTo,
                    subject = subject,
                    content = freemarkerBuilderUtil.build(ftl, model)!!.toByteArray(Charset.forName("UTF-8")),
                    status = TaskStatus.TODO)
            emailLogDao.save(emailLog)
        } catch (e: Exception) {
            log.error("email send error", e)
        }
    }


    fun update(customEmailLog: EmailLog) {
        emailLogDao.save(customEmailLog)
    }


    operator fun get(id: String): EmailLog {
        return emailLogDao.findOne(NumberUtils.createLong(id)!!)
    }

    operator fun get(id: Long?): EmailLog {
        return emailLogDao!!.findOne(id!!)
    }
}
