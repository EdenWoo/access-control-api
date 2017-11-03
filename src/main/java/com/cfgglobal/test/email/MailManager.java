package com.cfgglobal.test.email;


import com.cfgglobal.test.base.FreemarkerBuilderUtil;
import com.cfgglobal.test.dao.EmailLogDao;
import com.cfgglobal.test.domain.EmailLog;
import com.cfgglobal.test.enums.TaskStatus;
import io.vavr.collection.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;


@Service
public class MailManager {

    private static final Logger log = LoggerFactory.getLogger(MailManager.class);
    @Autowired
    private FreemarkerBuilderUtil freemarkerBuilderUtil;
    @Autowired
    private EmailLogDao emailLogDao;

    public void sendSystem(String subject, String sendTo, String ftl, Map<String, Object> model) {
        try {
            model = model.put("subject", subject);
            EmailLog emailLog = new EmailLog();
            emailLog.setTimes(0);
            emailLog.setSendTo(sendTo);
            emailLog.setSubject(subject);
            String content = freemarkerBuilderUtil.build(ftl, model.toJavaMap());
            emailLog.setContent(content.getBytes(Charset.forName("UTF-8")));
            emailLog.setStatus(TaskStatus.TODO);
            emailLogDao.save(emailLog);
        } catch (Exception e) {
            log.error("email send error", e);
        }

    }
}
