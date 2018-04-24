package com.github.leon.sms.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.enums.TaskStatus
import com.github.leon.sms.domain.MessageLog
import org.springframework.stereotype.Repository

@Repository
interface MessageLogDao : BaseDao<MessageLog, Long> {

    fun findByStatus(status: TaskStatus): List<MessageLog>

}
