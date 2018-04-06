package com.github.leon.sysconfig


import com.github.leon.aci.dao.base.BaseDao
import org.springframework.stereotype.Repository

@Repository
interface SysConfigDao : BaseDao<SysConfig, Long> {

    fun findByConfKey(key: String): SysConfig?

}
