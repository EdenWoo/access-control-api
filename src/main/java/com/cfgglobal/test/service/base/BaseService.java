package com.cfgglobal.test.service.base;

import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.service.rule.SecurityFilter;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


@NoRepositoryBean
public abstract class BaseService<T, ID extends Serializable> {


    protected BaseDao<T, ID> baseDao;

    @Autowired
    protected SecurityFilter securityFilter;

    @Autowired
    public void setBaseDao(BaseDao<T, ID> baseDao) {
        this.baseDao = baseDao;
    }

    public Page<T> findByRequestParameters(Map<String, String[]> info, Pageable pageable) {
        return baseDao.findByRequestParameters(info, pageable);
    }


    public List<T> findByRequestParameters(Map<String, String[]> info) {
        return baseDao.findByRequestParameters(info);
    }


    public Page<T> findByFilter(List<Filter> filters, Pageable pageable) {
        return baseDao.findByFilter(filters, pageable);
    }


    public List<T> findByFilter(List<Filter> filters) {
        return baseDao.findByFilter(filters);
    }

    public List<T> findByFilter(Filter filter) {
        return baseDao.findByFilter(List.of(filter));
    }


    @Transactional
    public Page<T> findBySecurity(String method, String requestURI, Map<String, String[]> params, Pageable pageable) {
        //   ConfigAttribute configAttribute = MyInvocationSecurityMetadataSourceService.getConfigAttributeDefinition();
        //  System.out.println(configAttribute);
        List<Filter> queryFilters = Filter.createFilters(params);
        List<Filter> securityFilters = securityFilter.query(method, requestURI);
        return baseDao.findByFilter(queryFilters.appendAll(securityFilters), pageable);
    }

    @Transactional
    public List<T> findBySecurity(String method, String requestURI, Map<String, String[]> params) {
        //   ConfigAttribute configAttribute = MyInvocationSecurityMetadataSourceService.getConfigAttributeDefinition();
        //  System.out.println(configAttribute);
        List<Filter> queryFilters = Filter.createFilters(params);
        List<Filter> securityFilters = securityFilter.query(method, requestURI);
        return baseDao.findByFilter(queryFilters.appendAll(securityFilters));
    }

    public T findOneBySecurity(Long id, String method, String requestURI) {
        List<Filter> securityFilters = securityFilter.query(method, requestURI);
        List<T> list = baseDao.findByFilter(securityFilters);
        T entity = baseDao.findById((ID) id).get();
        if (list.contains(entity)) {
            return entity;
        } else {
            throw new AccessDeniedException(requestURI);
        }
    }

    public void deleteBySecurity(Long id, String method, String requestURI) {
        List<Filter> securityFilters = securityFilter.query(method, requestURI);
        List<T> list = baseDao.findByFilter(securityFilters);
        T entity = baseDao.findById((ID) id).get();
        if (list.contains(entity)) {
            baseDao.delete(entity);
        } else {
            throw new AccessDeniedException(requestURI);
        }
    }

    public <S extends T> S saveBySecurity(S entity, String method, String requestURI) {
        Object id = Reflect.on(entity).get("id");
        if (id == null || baseDao.findByFilter(securityFilter.query(method, requestURI)).contains(entity)) {
            return save(entity);
        }
        throw new AccessDeniedException(requestURI);
    }


    public Page<T> findAll(Pageable pageable) {
        return baseDao.findAll(pageable);
    }

    public List<T> findAll() {
        return List.ofAll(baseDao.findAll());
    }

    public List<T> findAll(Sort sort) {
        return List.ofAll(baseDao.findAll(sort));
    }

    public List<T> findAll(Iterable<ID> ids) {
        return List.ofAll(baseDao.findAll(ids));
    }

    public long count() {
        return baseDao.count();
    }

    public void delete(ID id) {
        baseDao.delete(id);
    }

    public void delete(T entity) {
        baseDao.delete(entity);
    }


    public void deleteAll() {
        baseDao.deleteAll();
    }

    public <S extends T> List<S> save(Iterable<S> entities) {
        return List.ofAll(baseDao.save(entities));
    }

    public void flush() {
        baseDao.flush();
    }

    public <S extends T> S saveAndFlush(S entity) {
        return baseDao.saveAndFlush(entity);
    }

    public void deleteInBatch(Iterable<T> entities) {
        baseDao.deleteInBatch(entities);
    }

    public void deleteAllInBatch() {
        baseDao.deleteAllInBatch();
    }

    public <S extends T> List<S> findAll(Example<S> example) {
        return List.ofAll(baseDao.findAll(example));
    }

    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return List.ofAll(baseDao.findAll(example, sort));
    }


    public List<T> findAll(Specification<T> spec) {
        return List.ofAll(baseDao.findAll(spec));
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return baseDao.findAll(spec, pageable);
    }


    public List<T> findAll(Specification<T> spec, Sort sort) {
        return List.ofAll(baseDao.findAll(spec, sort));
    }

    public long count(Specification<T> spec) {
        return baseDao.count(spec);
    }

    public <S extends T> S save(S entity) {
        return baseDao.save(entity);
    }

    public T findOne(ID id) {
        return baseDao.findById(id).get();
    }

}


