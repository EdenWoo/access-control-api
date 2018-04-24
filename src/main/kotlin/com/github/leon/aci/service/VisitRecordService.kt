package com.github.leon.aci.service

import com.github.leon.aci.dao.VisitRecordDao
import com.github.leon.aci.domain.User
import com.github.leon.aci.domain.VisitRecord
import com.github.leon.aci.service.base.BaseService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class VisitRecordService(
        @Autowired val visitRecordDao: VisitRecordDao
) : BaseService<VisitRecord, Long>() {
    val log = LoggerFactory.getLogger(VisitRecordService::class.java)!!

    fun hasTooManyRequest(user: Optional<User>, ip: String): Boolean {
        if (user.isPresent) {
            val userVisitRecords = visitRecordDao.findAllInLastMinute(user.get().id as Long)
            log.debug("user visit records size {}", userVisitRecords.size)
            if (userVisitRecords.size > THRESHOLD) {
                return true
            }
        } else {
            val ipVisitRecords = visitRecordDao.findAllInLastMinute(ip)
            log.debug("ip visit records size {}", ipVisitRecords.size)
            if (ipVisitRecords.size > THRESHOLD) {
                return true
            }
        }
        return false

    }

    companion object {

        const val THRESHOLD: Long = 1000
    }
}
