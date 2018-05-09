package com.github.leon.sysconfig.service

import com.github.leon.aci.service.base.BaseService
import com.github.leon.setting.dao.SettingDao
import com.github.leon.sysconfig.domain.SysConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class SysConfigService(

        @Autowired
        val settingDao: SettingDao
) : BaseService<SysConfig, Long>() {

    fun isWorkingHour(localTime: LocalTime): Boolean {
        val setting = settingDao.findByActive(true)
        return localTime.hour in setting.workHoursStart..(setting.workHoursEnd.dec())
    }

}
