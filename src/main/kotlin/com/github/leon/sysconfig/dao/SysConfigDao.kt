package com.github.leon.sysconfig.dao


import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.sysconfig.domain.SysConfig
import org.springframework.stereotype.Repository

@Repository
interface SysConfigDao : BaseDao<SysConfig, Long> {

    fun findByConfKey(key: String): SysConfig?

}
