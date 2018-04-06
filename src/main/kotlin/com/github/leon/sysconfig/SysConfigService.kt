package com.github.leon.sysconfig

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.github.leon.aci.domain.User
import com.github.leon.aci.service.base.BaseService
import com.github.leon.date.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class SysConfigService(
        @Autowired
        val sysConfigDao: SysConfigDao
) : BaseService<SysConfig, Long>() {
    fun canTrade(user: User): Boolean {
        val canTradeConfig: Boolean? = sysConfigDao
                .findByConfKey("sys_trade_switcher")
                .toOption()
                .map { it.confVal }
                .map { it.toBoolean() }
                .getOrElse { true }
        return user.verify!! && canTradeConfig!! && DateUtils.isWeekday(LocalDate.now()) && isWorkingHour(LocalTime.now())
    }

    /* fun <T>findByKey(key: String, default: Boolean): T {
         sysConfigDao.findByConfKey(key)
                 .toOption()
                 .map { it.confVal }
                 .map { it.toBoolean() }
                 .getOrElse { default }

     }*/
    companion object {

        private const val OPEN = 8
        private const val CLOSE = 22

        private fun isWorkingHour(localTime: LocalTime): Boolean {
            return localTime.hour in OPEN..(CLOSE - 1)
        }
    }
}
