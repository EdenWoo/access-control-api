package com.cfgglobal.test.service;

import com.cfgglobal.test.dao.EmailLogDao;
import com.cfgglobal.test.domain.EmailLog;
import com.cfgglobal.test.service.base.BaseService;
import io.vavr.API;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
public class EmailLogService extends BaseService<EmailLog, Long> {

    @Autowired
    private EmailLogDao emailLogDao;


    public List<EmailLog> findForSend(EmailLog customEmail) {
        return emailLogDao.findAll((root, query, cb) -> {
            io.vavr.collection.List<Predicate> predicates = API.List();
            if (customEmail.getStatus() != null) {
                Predicate p = cb.equal(root.get("status"), customEmail.getStatus());
                predicates = predicates.append(p);
            }
            if (customEmail.getTimes() != null) {
                Predicate p2 = cb.lessThan(root.get("times"), customEmail.getTimes());
                predicates = predicates.append(p2);
            }
            return predicates.isEmpty() ? null : predicates.reduce(cb::and);

        });
    }


    public void update(EmailLog customEmailLog) {
        emailLogDao.save(customEmailLog);
    }


    public EmailLog get(String id) {
        return emailLogDao.findById(NumberUtils.createLong(id)).get();
    }

    public EmailLog get(Long id) {
        return emailLogDao.findById(id).get();
    }
}
