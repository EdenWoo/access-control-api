package com.github.leon.sysconfig.service

import arrow.core.getOrElse
import arrow.core.toOption
import com.github.leon.aci.domain.User
import com.github.leon.aci.service.base.BaseService
import com.github.leon.date.DateUtils
import com.github.leon.setting.dao.SettingDao
import com.github.leon.sysconfig.dao.SysConfigDao
import com.github.leon.sysconfig.domain.SysConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class SysConfigService(
        @Autowired
        val sysConfigDao: SysConfigDao,
        @Autowired
        val settingDao: SettingDao
) : BaseService<SysConfig, Long>() {
    fun canTrade(user: User): Boolean {
        val canTradeConfig: Boolean? = sysConfigDao
                .findByConfKey("sys_trade_switcher")
                .toOption()
                .map { it.confVal }
                .map { it.toBoolean() }
                .getOrElse { true }

        return user.verify!!
                && canTradeConfig!!
                && DateUtils.isWeekday(LocalDate.now())
                && isWorkingHour(LocalTime.now())
    }


    fun isWorkingHour(localTime: LocalTime): Boolean {
        val setting = settingDao.findByActive(true)
        return localTime.hour in setting.startWorkHour..(setting.endWorkHour.dec())
    }

}
