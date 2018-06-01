package com.github.leon.email.service

import com.github.leon.aci.enums.TaskStatus
import com.github.leon.aci.service.base.BaseService
import com.github.leon.aci.vo.Condition
import com.github.leon.aci.vo.Filter
import com.github.leon.email.dao.EmailLogDao
import com.github.leon.email.domain.EmailLog
import com.github.leon.fm.FreemarkerBuilderUtil
import com.github.leon.setting.dao.SettingDao
import org.apache.commons.lang3.math.NumberUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class EmailLogService(

        @Autowired
        val emailLogDao: EmailLogDao,
        @Autowired
        val settingDao: SettingDao,
        @Autowired
        val freemarkerBuilderUtil: FreemarkerBuilderUtil,
        @Autowired
        val emailServerDao: EmailLogDao
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

    fun send(emailLog: EmailLog): Pair<String, Boolean> {
        val emailServer = settingDao.findByActive(true)!!.emailServer!!
        val sender = createSender()
        return try {
            val mailMessage = sender.createMimeMessage()
            val messageHelper = MimeMessageHelper(mailMessage, true, "UTF-8")
            messageHelper.setFrom(emailServer.fromAddress, emailServer.fromAddress)
            messageHelper.setTo(emailLog.sendTo!!)
            messageHelper.setSubject(emailLog.subject!!)
            messageHelper.setText(String(emailLog.content!!), true)
            sender.send(mailMessage)
            Pair("", true)
        } catch (e: Exception) {
            log.error("EmailLog Send Error:" + emailLog.sendTo!!, e)
            Pair(e.message!!, false)
        }

    }

    private fun createSender(): JavaMailSender {
        val emailServer = settingDao.findByActive(true)!!.emailServer!!
        val sender = JavaMailSenderImpl()
        sender.host = emailServer.host
        sender.port = emailServer.port
        sender.username = emailServer.username
        sender.password = emailServer.password
        sender.javaMailProperties.setProperty("mail.smtp.starttls.enable", "true")
        sender.javaMailProperties.setProperty("mail.smtp.auth", "true")
        sender.javaMailProperties.setProperty("mail.smtp.timeout", emailServer.timeout.toString())
        sender.javaMailProperties.setProperty("mail.smtp.ssl.trust", emailServer.host)
        sender.javaMailProperties.setProperty("mail.smtp.socketFactory.fallback", "false")
        return sender
    }

    fun sendSystem(orderId: String = "", subject: String, sendTo: String, ftl: String, model: Map<String, Any?>) {
        val setting = settingDao.findByActive(true)
        val m = model.toMutableMap()
        m["setting"] = setting
        try {
            val emailLog = EmailLog(
                    orderId = orderId,
                    times = 0,
                    sendTo = sendTo,
                    subject = subject,
                    content = freemarkerBuilderUtil.build(ftl, m)!!.toByteArray(Charset.forName("UTF-8")),
                    status = TaskStatus.TODO)
            emailLogDao.save(emailLog)
        } catch (e: Exception) {
            log.error("email send error", e)
        }
    }


    fun update(customEmailLog: EmailLog) {
        emailLogDao.save(customEmailLog)
    }


    fun execute() {
        var email = EmailLog(status = TaskStatus.TODO)
        var list = findForSend(email)

        email = EmailLog(
                status = TaskStatus.FAILURE,
                times = 3
        )
        list += (findForSend(email))
        list.forEach { item ->
            val resultVO = send(item)
            val result: EmailLog
            result = when {
                resultVO.second -> item.copy(
                        status = TaskStatus.SUCCESS
                ).apply {
                    this.id = item.id
                    this.version = item.version
                }
                else -> item.copy(
                        status = TaskStatus.FAILURE,
                        times = item.times!!.inc(),
                        msg = resultVO.first
                ).apply {
                    this.id = item.id
                    this.version = item.version
                }
            }
            update(result)
        }
    }

    fun get(id: String): EmailLog {
        return emailLogDao.findOne(NumberUtils.createLong(id)!!)
    }

    fun get(id: Long?): EmailLog {
        return emailLogDao.findOne(id!!)
    }
}
