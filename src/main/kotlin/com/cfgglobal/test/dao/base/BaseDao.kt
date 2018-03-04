package com.cfgglobal.test.dao.base

import com.cfgglobal.test.vo.Filter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable
import java.util.*


@NoRepositoryBean
interface BaseDao<T, ID : Serializable> : JpaRepository<T, ID>, JpaSpecificationExecutor<T> {


    fun findByRequestParameters(info: Map<String, Array<String>>, pageable: Pageable): Page<T>

    fun findByRequestParameters(info: Map<String, Array<String>>): List<T>


    fun findByFilter(filters: List<Filter>, pageable: Pageable): Page<T>

    fun findByFilter(filters: List<Filter>): List<T>

    fun findByFilter(filter: Filter): List<T>

    fun findAll(spec: Specification<T>, pageable: Pageable, entityGraphName: String): Page<T>

    fun support(modelType: String): Boolean

    fun findById(id: ID): Optional<T>
}