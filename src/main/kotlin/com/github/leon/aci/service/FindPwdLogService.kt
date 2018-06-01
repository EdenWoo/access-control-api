package com.github.leon.aci.service


import com.github.leon.aci.dao.FindPwdSendLogDao
import com.github.leon.aci.domain.FindPwdSendLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime


@Repository
class  FindPwdLogService(
        @Autowired
        val findPwdSendLogDao: FindPwdSendLogDao

) {
    fun insert(log: FindPwdSendLog) {
        log.expireDate= ZonedDateTime.now().plusDays(1)
        findPwdSendLogDao.save(log)
    }

    fun update(log: FindPwdSendLog) {

        findPwdSendLogDao.save(log)
    }

    fun getLogById(id: Long): FindPwdSendLog {
        return findPwdSendLogDao.getOne(id)
    }
}
