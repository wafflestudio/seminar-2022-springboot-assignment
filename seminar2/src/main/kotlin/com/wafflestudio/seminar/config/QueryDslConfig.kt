package com.wafflestudio.seminar.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class QueryDslConfig(
    @PersistenceContext
    val entityManager: EntityManager,
) {
    @Bean
    fun queryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}