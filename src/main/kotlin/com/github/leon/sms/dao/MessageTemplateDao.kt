package com.github.leon.sms.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.sms.domain.MessageTemplate
import org.springframework.stereotype.Repository

@Repository
interface MessageTemplateDao : BaseDao<MessageTemplate, Long> {


}
