package com.cfgglobal.test.config.jpa;

import com.cfgglobal.test.dao.base.BaseDaoFactoryBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseDaoFactoryBean.class, basePackages = "com.cfgglobal.test.dao")
@EntityScan(basePackages = "com.cfgglobal.test.domain")
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {


    @Autowired
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }


    @Bean
    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }
}
