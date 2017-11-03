package com.cfgglobal.test.email;

import com.cfgglobal.test.domain.EmailLog;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;


@Component
public class CustomMailUtil {
    private static Logger log = LoggerFactory.getLogger(CustomMailUtil.class);


    @Value("${mail.sender.timeout}")
    private String timeout;

    @Value("${mail.sender.host}")
    private String host;

    @Value("${mail.sender.port}")
    private int port;

    @Value("${mail.sender.alias}")
    private String alias;

    @Value("${mail.sender.from}")
    private String from;

    @Value("${mail.sender.username}")
    private String username;

    @Value("${mail.sender.password}")
    private String password;


    private JavaMailSender createSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
        sender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");
        sender.getJavaMailProperties().setProperty("mail.smtp.timeout", timeout);
        sender.getJavaMailProperties().setProperty("mail.smtp.ssl.trust", host);
        sender.getJavaMailProperties().setProperty("mail.smtp.socketFactory.fallback", "false");
        return sender;
    }

    public Tuple2<String, Boolean> send(EmailLog emailItemVO) {

        JavaMailSender sender = createSender();
        try {
            MimeMessage mailMessage = sender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            messageHelper.setFrom(from, from);
            messageHelper.setTo(emailItemVO.getSendTo());
            messageHelper.setSubject(emailItemVO.getSubject());

            messageHelper.setText(new String(emailItemVO.getContent()), true);
            sender.send(mailMessage);

            return Tuple.of("", true);
        } catch (Exception e) {
            log.error("EmailLog Send Error:" + emailItemVO.getSendTo(), e);
            return Tuple.of(e.getMessage(), false);
        }
    }
}