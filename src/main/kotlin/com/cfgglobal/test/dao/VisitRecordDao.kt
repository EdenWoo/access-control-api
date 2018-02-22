package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.VisitRecord
import io.vavr.collection.List
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VisitRecordDao : BaseDao<VisitRecord, Long> {

    @Query(nativeQuery = true, value = "select * from visit_record where created_at between date_sub(now(), interval 1 minute) and now() and creator_id=?1")
    fun findAllInLastMinute(creatorId: Long): List<VisitRecord>

    @Query(nativeQuery = true, value = "select * from visit_record where created_at between date_sub(now(), interval 1 minute) and now() and ip=?1")
    fun findAllInLastMinute(ip: String): List<VisitRecord>
}