package com.github.leon.aci.dao


import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.FindPwdSendLog
import org.springframework.stereotype.Repository

@Repository
interface FindPwdSendLogDao : BaseDao<FindPwdSendLog, Long>
