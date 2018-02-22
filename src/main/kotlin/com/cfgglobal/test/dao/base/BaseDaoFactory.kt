package com.cfgglobal.test.dao.base

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import java.io.Serializable
import javax.persistence.EntityManager

class BaseDaoFactory<S, ID : Serializable>(entityManager: EntityManager) : JpaRepositoryFactory(entityManager) {

    override fun <T, ID : Serializable> getTargetRepository(metadata: RepositoryInformation,
                                                            entityManager: EntityManager): SimpleJpaRepository<*, *> {
        return BaseDaoImpl<T, ID>(metadata.domainType as Class<T>, entityManager)
    }

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return BaseDao::class.java
    }

}