package com.cfgglobal.test.email

import com.cfgglobal.test.domain.EmailLog
import io.vavr.Tuple
import io.vavr.Tuple2
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component


@Component
class CustomMailUtil {


    @Value("\${mail.sender.timeout}")
    private val timeout: String? = null

    @Value("\${mail.sender.host}")
    private val host: String? = null

    @Value("\${mail.sender.port}")
    private val port: Int = 0

    @Value("\${mail.sender.alias}")
    private val alias: String? = null

    @Value("\${mail.sender.from}")
    private val from: String? = null

    @Value("\${mail.sender.username}")
    private val username: String? = null

    @Value("\${mail.sender.password}")
    private val password: String? = null


    private fun createSender(): JavaMailSender {
        val sender = JavaMailSenderImpl()
        sender.host = host
        sender.port = port
        sender.username = username
        sender.password = password
        sender.javaMailProperties.setProperty("mail.smtp.starttls.enable", "true")
        sender.javaMailProperties.setProperty("mail.smtp.auth", "true")
        sender.javaMailProperties.setProperty("mail.smtp.timeout", timeout)
        sender.javaMailProperties.setProperty("mail.smtp.ssl.trust", host)
        sender.javaMailProperties.setProperty("mail.smtp.socketFactory.fallback", "false")
        return sender
    }

    fun send(emailItemVO: EmailLog): Tuple2<String, Boolean> {

        val sender = createSender()
        try {
            val mailMessage = sender.createMimeMessage()
            val messageHelper = MimeMessageHelper(mailMessage, true, "UTF-8")
            messageHelper.setFrom(from!!, from)
            messageHelper.setTo(emailItemVO.sendTo!!)
            messageHelper.setSubject(emailItemVO.subject!!)

            messageHelper.setText(String(emailItemVO.content!!), true)
            sender.send(mailMessage)

            return Tuple.of("", true)
        } catch (e: Exception) {
            log.error("EmailLog Send Error:" + emailItemVO.sendTo!!, e)
            return Tuple.of(e.message, false)
        }

    }

    companion object {
        private val log = LoggerFactory.getLogger(CustomMailUtil::class.java)
    }
}