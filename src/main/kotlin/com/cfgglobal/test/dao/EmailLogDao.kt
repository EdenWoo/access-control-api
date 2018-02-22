package com.cfgglobal.test.dao


import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.EmailLog
import org.springframework.stereotype.Repository


@Repository
interface EmailLogDao : BaseDao<EmailLog, Long>

