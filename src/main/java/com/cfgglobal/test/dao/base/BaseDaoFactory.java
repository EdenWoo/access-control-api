package com.cfgglobal.test.dao.base;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class BaseDaoFactory<S, ID extends Serializable> extends JpaRepositoryFactory {

    public BaseDaoFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "hiding"})
    @Override
    protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation metadata,
                                                                                         EntityManager entityManager) {
        return new BaseDaoImpl(metadata.getDomainType(), entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return BaseDao.class;
    }

}