package com.cfgglobal.test.dao.base;

import com.cfgglobal.test.base.Filter;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;


@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {


    Page<T> findByRequestParameters(Map<String, String[]> info, Pageable pageable);

    List<T> findByRequestParameters(Map<String, String[]> info);


    Page<T> findByFilter(List<Filter> filters, Pageable pageable);

    List<T> findByFilter(List<Filter> filters);

    List<T> findByFilter(Filter filter);

    boolean support(String modelType);

    Optional<T> findById(ID id);
}