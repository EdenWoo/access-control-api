package com.github.leon.email


import com.github.leon.email.dao.EmailLogDao
import com.github.leon.email.domain.EmailLog
import com.github.leon.aci.enums.TaskStatus
import com.github.leon.freemarker.FreemarkerBuilderUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.Charset


@Service
class MailManager {
    private val log = LoggerFactory.getLogger(MailManager::class.java)
    @Autowired
    private val freemarkerBuilderUtil: FreemarkerBuilderUtil? = null
    @Autowired
    private val emailLogDao: EmailLogDao? = null

    fun sendSystem(subject: String, sendTo: String, ftl: String, model: Map<String, Any?>) {
        try {
            val emailLog = EmailLog(
                    times = 0,
                    sendTo = sendTo,
                    subject = subject,
                    content = freemarkerBuilderUtil!!.build(ftl, model)!!.toByteArray(Charset.forName("UTF-8")),
                    status = TaskStatus.TODO)
            emailLogDao!!.save(emailLog)
        } catch (e: Exception) {
            log.error("email send error", e)
        }

    }

}
