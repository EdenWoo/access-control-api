package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.Attachment
import org.springframework.stereotype.Repository

@Repository
interface AttachmentDao : BaseDao<Attachment, Long>
