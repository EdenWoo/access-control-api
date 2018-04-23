package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.VisitRecord

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VisitRecordDao : BaseDao<VisitRecord, Long> {

    @Query(nativeQuery = true, value = "select * fromAddress visit_record where created_at between date_sub(now(), interval 1 minute) and now() and creator_id=?1")
    fun findAllInLastMinute(creatorId: Long): List<VisitRecord>

    @Query(nativeQuery = true, value = "select * fromAddress visit_record where created_at between date_sub(now(), interval 1 minute) and now() and ip=?1")
    fun findAllInLastMinute(ip: String): List<VisitRecord>
}