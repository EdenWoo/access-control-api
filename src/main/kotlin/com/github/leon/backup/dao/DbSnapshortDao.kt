package com.github.leon.backup.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.setting.domain.Setting
import org.springframework.stereotype.Repository

@Repository
interface DbSnapshortDao : BaseDao<Setting, Long> {
}
