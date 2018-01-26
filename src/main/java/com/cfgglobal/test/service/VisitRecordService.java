package com.cfgglobal.test.service;

import com.cfgglobal.test.dao.VisitRecordDao;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.domain.VisitRecord;
import com.cfgglobal.test.service.base.BaseService;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VisitRecordService extends BaseService<VisitRecord, Long> {


    @Autowired
    private VisitRecordDao visitRecordDao;

    public void needBlock(User user, String ip) {
        List<VisitRecord> userVisitRecords = visitRecordDao.findAllInLastMinute(user.getId());
        List<VisitRecord> ipVisitRecords = visitRecordDao.findAllInLastMinute(ip);

    }
}
