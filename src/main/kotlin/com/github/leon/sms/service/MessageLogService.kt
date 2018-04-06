package com.github.leon.sms.service

import arrow.core.None
import arrow.core.Some
import arrow.core.getOrElse
import arrow.syntax.collections.firstOption
import arrow.syntax.option.toOption
import com.github.leon.aci.enums.TaskStatus
import com.github.leon.aci.service.base.BaseService
import com.github.leon.email.dao.EmailLogDao
import com.github.leon.email.domain.EmailLog
import com.github.leon.sms.dao.MessageLogDao
import com.github.leon.sms.domain.MessageLog
import com.github.leon.sysconfig.SysConfigDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MessageLogService(
        @Autowired
        var messageProviders: List<MessageProvider>,
        @Autowired
        val messageLogDao: MessageLogDao,
        @Autowired
        val emailLogDao: EmailLogDao,
        @Autowired
        val sysConfigDao: SysConfigDao

) : BaseService<MessageLog, Long>() {

    fun send(message: MessageLog): Pair<String?, String> {
        val enableProvider = sysConfigDao.findByConfKey("sms_provider").toOption().map { it.confVal }.getOrElse { "clicktale" }
        val providerOpt = messageProviders.firstOption {
            it.javaClass.simpleName.substringBefore(MessageProvider::class.java.simpleName).equals(enableProvider, ignoreCase = true)
        }
        return when (providerOpt) {
            is Some -> providerOpt.t.send(message)
            None -> throw IllegalStateException("sms provider missing")
        }
    }

    @Transactional
    fun sendMessage() {
        val notSentMessages = messageLogDao.findByStatus(TaskStatus.TODO)
        for (smsMessage in notSentMessages) {

            smsMessage.sendTo = dealMobileNum(smsMessage.sendTo)

            val (error, response) = send(smsMessage)
            smsMessage.resp = response
            if (error == null) {
                smsMessage.status = TaskStatus.SUCCESS
            } else {
                smsMessage.status = TaskStatus.FAILURE
                val emailLog = EmailLog(
                        sendTo = "lei.zhou@cfgglobal.co.nz",
                        content = "Sms no credit".toByteArray()
                )
                emailLogDao.save(emailLog)
            }
            messageLogDao.save(smsMessage)
        }
    }


    private fun dealMobileNum(number: String): String {
        var number = number

        if (number.startsWith("0")) {
            number = number.substring(1).trim { it <= ' ' }
        }
        if (number.startsWith("00")) {
            number = number.substring(2).trim { it <= ' ' }
        }
        if (number.startsWith("2")) {
            number = "64" + number
        }
        if (number.startsWith("4")) {
            number = "61" + number
        }
        if (number.startsWith("1")) {
            number = "86" + number
        }

        return number
    }


}
