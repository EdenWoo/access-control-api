package com.github.leon.aci.dao

import com.github.leon.aci.dao.base.BaseDao
import com.github.leon.aci.domain.Branch

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BranchDao : BaseDao<Branch, Long> {


    @Query("SELECT id from Branch where parent_id = :id ")
    fun findSubOrgIds(@Param("id") id: Long?): List<Long>

}