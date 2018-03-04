package com.cfgglobal.test.service.base


import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.service.rule.SecurityFilter
import com.cfgglobal.test.vo.Filter
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
abstract class BaseService<T, ID : Serializable> {
    @Autowired
    lateinit var baseDao: BaseDao<T, ID>

    @Autowired
    lateinit var securityFilter: SecurityFilter

    /* fun setBaseDao(baseDao: BaseDao<T, ID>) {
         this.baseDao = baseDao
     }*/

    fun findByRequestParameters(info: Map<String, Array<String>>, pageable: Pageable): Page<T> {
        return baseDao.findByRequestParameters(info, pageable)
    }


    fun findByRequestParameters(info: Map<String, Array<String>>): List<T> {
        return baseDao.findByRequestParameters(info)
    }


    fun findByFilter(filters: List<Filter>, pageable: Pageable): Page<T> {
        return baseDao.findByFilter(filters, pageable)
    }


    fun findByFilter(filters: List<Filter>): List<T> {
        return baseDao.findByFilter(filters)
    }

    fun findByFilter(filter: Filter): List<T> {
        return baseDao.findByFilter(listOf(filter))
    }


    fun findBySecurity(method: String, requestURI: String, params: Map<String, Array<String>>, pageable: Pageable): Page<T> {
        //  ConfigAttribute configAttribute = MyInvocationSecurityMetadataSourceService.getConfigAttributeDefinition();
        //  System.out.println(configAttribute);
        val queryFilters = Filter.createFilters(params)
        val securityFilters = securityFilter.query(method, requestURI)
        return baseDao.findByFilter(queryFilters + securityFilters, pageable)
    }

    fun findBySecurity(method: String, requestURI: String, params: Map<String, Array<String>>): List<T> {
        //   ConfigAttribute configAttribute = MyInvocationSecurityMetadataSourceService.getConfigAttributeDefinition();
        //  System.out.println(configAttribute);
        val queryFilters = Filter.createFilters(params)
        val securityFilters = securityFilter.query(method, requestURI)
        return baseDao.findByFilter(queryFilters + securityFilters)
    }

    fun findOneBySecurity(id: ID, method: String, requestURI: String): T {
        val securityFilters = securityFilter.query(method, requestURI)
        val list = baseDao.findByFilter(securityFilters)
        val entity = baseDao.findOne(id)
        return if (list.contains(entity)) {
            entity
        } else {
            throw AccessDeniedException(requestURI)
        }
    }

    fun deleteBySecurity(id: ID, method: String, requestURI: String) {
        val securityFilters = securityFilter!!.query(method, requestURI)
        val list = baseDao.findByFilter(securityFilters)
        val entity = baseDao.findOne(id)
        if (list.contains(entity)) {
            baseDao.delete(entity)
        } else {
            throw AccessDeniedException(requestURI)
        }
    }

    fun <S : T> saveBySecurity(entity: S, method: String, requestURI: String): S {
        val id = Reflect.on(entity).get<Any>("id")
        if (id == null || baseDao.findByFilter(securityFilter!!.query(method, requestURI)).contains(entity)) {
            return save(entity)
        }
        throw AccessDeniedException(requestURI)
    }


    fun findAll(pageable: Pageable): Page<T> {
        return baseDao.findAll(pageable)
    }

    fun findAll(): List<T> {
        return baseDao.findAll()
    }

    fun findAll(sort: Sort): List<T> {
        return baseDao.findAll(sort)
    }

    fun findAll(ids: Iterable<ID>): List<T> {
        return baseDao.findAll(ids)
    }

    fun count(): Long {
        return baseDao.count()
    }

    fun delete(id: ID) {
        baseDao.delete(id)
    }

    fun delete(entity: T) {
        baseDao.delete(entity)
    }


    fun deleteAll() {
        baseDao.deleteAll()
    }

    fun <S : T> save(entities: Iterable<S>): List<S> {
        return baseDao.save(entities)
    }

    fun flush() {
        baseDao.flush()
    }

    fun <S : T> saveAndFlush(entity: S): S {
        return baseDao.saveAndFlush(entity)
    }

    fun deleteInBatch(entities: Iterable<T>) {
        baseDao.deleteInBatch(entities)
    }

    fun deleteAllInBatch() {
        baseDao.deleteAllInBatch()
    }

    fun <S : T> findAll(example: Example<S>): List<S> {
        return baseDao.findAll(example)
    }

    fun <S : T> findAll(example: Example<S>, sort: Sort): List<S> {
        return baseDao.findAll(example, sort)
    }


    fun findAll(spec: Specification<T>): List<T> {
        return baseDao.findAll(spec)
    }

    fun findAll(spec: Specification<T>, pageable: Pageable): Page<T> {
        return baseDao.findAll(spec, pageable)
    }


    fun findAll(spec: Specification<T>, sort: Sort): List<T> {
        return baseDao.findAll(spec, sort)
    }

    fun count(spec: Specification<T>): Long {
        return baseDao.count(spec)
    }

    fun <S : T> save(entity: S): S {
        return baseDao.save(entity)
    }

    fun findOne(id: ID): T {
        return baseDao.findOne(id)
    }

}


