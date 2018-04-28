package com.github.leon.aci.service


import com.github.leon.aci.dao.FindPwdSendLogDao
import com.github.leon.aci.domain.FindPwdSendLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class  FindPwdLogService(
        @Autowired
        val findPwdSendLogDao: FindPwdSendLogDao

) {
    fun insert(log: FindPwdSendLog) {
        val calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 24);      //24小时
        val date =  calendar.time
        log.expireDate= date.time
        findPwdSendLogDao.save(log)
    }

    fun update(log: FindPwdSendLog) {

        findPwdSendLogDao.save(log)
    }

    fun getLogById(id: Long): FindPwdSendLog {
        return findPwdSendLogDao.getOne(id)
    }
}
