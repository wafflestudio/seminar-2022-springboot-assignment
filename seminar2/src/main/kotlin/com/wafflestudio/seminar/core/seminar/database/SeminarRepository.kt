package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import org.springframework.stereotype.Repository

interface SeminarRepository : JpaRepository<SeminarEntity, Long>, SeminarSupport

interface SeminarSupport {
    fun findByNameLatest(name: String?): MutableList<SeminarEntity>
}

@Repository
class SeminarSupportImpl(
    val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(SeminarEntity::class.java), SeminarSupport {
    
    override fun findByNameLatest(name: String?): MutableList<SeminarEntity> {
        return jpaQueryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.name.contains(name))
            .orderBy(seminarEntity.createdAt.desc())
            .fetch()
    }
    
}