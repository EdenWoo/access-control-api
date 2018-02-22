package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.Branch
import io.vavr.collection.List
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BranchDao : BaseDao<Branch, Long> {


    @Query("SELECT id from Branch where parent_id = :id ")
    fun findSubOrgIds(@Param("id") id: Long?): List<Long>

}