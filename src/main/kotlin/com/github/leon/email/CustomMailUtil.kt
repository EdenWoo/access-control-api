package com.github.leon.email

import com.github.leon.email.domain.EmailLog
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component


@Component
class CustomMailUtil {

    @Value("\${mail.sender.timeout}")
    val timeout: String? = null
    @Value("\${mail.sender.host}")
    val host: String? = null

    @Value("\${mail.sender.port}")
    val port: Int = 0

    @Value("\${mail.sender.alias}")
    val alias: String? = null

    @Value("\${mail.sender.from}")
    val from: String? = null

    @Value("\${mail.sender.username}")
    val username: String? = null

    @Value("\${mail.sender.password}")
    val password: String? = null

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

    fun send(emailItemVO: EmailLog): Pair<String, Boolean> {

        val sender = createSender()
        return try {
            val mailMessage = sender.createMimeMessage()
            val messageHelper = MimeMessageHelper(mailMessage, true, "UTF-8")
            messageHelper.setFrom(from!!, from)
            messageHelper.setTo(emailItemVO.sendTo!!)
            messageHelper.setSubject(emailItemVO.subject!!)

            messageHelper.setText(String(emailItemVO.content!!), true)
            sender.send(mailMessage)

            Pair("", true)
        } catch (e: Exception) {
            log.error("EmailLog Send Error:" + emailItemVO.sendTo!!, e)
            Pair(e.message!!, false)
        }

    }

    companion object {
        private val log = LoggerFactory.getLogger(CustomMailUtil::class.java)
    }
}