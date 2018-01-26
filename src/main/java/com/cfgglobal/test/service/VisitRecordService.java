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


    public static final long THRESHOLD = 500;
    @Autowired
    private VisitRecordDao visitRecordDao;

    public boolean hasTooManyRequest(User user, String ip) {
        if (user != null) {
            List<VisitRecord> userVisitRecords = visitRecordDao.findAllInLastMinute(user.getId());
            log.debug("user visit records size {}", userVisitRecords.length());
            if(userVisitRecords.size() > THRESHOLD){
                return true;
            }
        }else {
            List<VisitRecord> ipVisitRecords = visitRecordDao.findAllInLastMinute(ip);
            log.debug("ip visit records size {}", ipVisitRecords.length());
            if (ipVisitRecords.size() > THRESHOLD) {
                return true;
            }
        }
        return false;

    }
}
