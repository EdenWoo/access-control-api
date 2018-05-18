package com.github.leon.setting.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.setting.domain.Setting
import org.springframework.stereotype.Repository

@Repository
interface SettingDao : BaseDao<Setting, Long> {
    fun findByActive(active: Boolean): Setting?
}
