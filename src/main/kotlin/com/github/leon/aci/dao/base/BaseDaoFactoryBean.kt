package com.github.leon.aci.dao.base

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import java.io.Serializable
import javax.persistence.EntityManager

class BaseDaoFactoryBean<R : JpaRepository<S, ID>, S, ID : Serializable>(repositoryInterface: Class<out R>) : JpaRepositoryFactoryBean<R, S, ID>(repositoryInterface) {

    override fun createRepositoryFactory(entityManager: EntityManager): RepositoryFactorySupport {
        return BaseDaoFactory<Any, ID>(entityManager)
    }

}