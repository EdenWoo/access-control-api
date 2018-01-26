package com.cfgglobal.test.dao;

import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.VisitRecord;
import io.vavr.collection.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRecordDao extends BaseDao<VisitRecord, Long> {

    @Query("SELECT * FROM VISIT_RECORD WHERE CREATED_AT BETWEEN DATE_SUB(NOW(), INTERVAL 1 MINUTE) AND NOW() AND CREATOR_ID=?1")
    List<VisitRecord> findAllInLastMinute(long creatorId);

    @Query("SELECT * FROM VISIT_RECORD WHERE CREATED_AT BETWEEN DATE_SUB(NOW(), INTERVAL 1 MINUTE) AND NOW() AND IP=?1")
    List<VisitRecord> findAllInLastMinute(String ip);
}