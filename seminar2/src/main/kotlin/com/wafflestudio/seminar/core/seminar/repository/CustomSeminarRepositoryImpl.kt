package com.wafflestudio.seminar.core.seminar.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.stereotype.Repository

@Repository
class CustomSeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomSeminarRepository {

    override fun findByNameWithOrder(name: String, order: String): List<SeminarEntity> {
        val seminarEntity = QSeminarEntity.seminarEntity
        return queryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.name.contains(name))
            .orderBy(
                when (order) {
                    "earliest" -> seminarEntity.createdAt.asc()
                    else -> seminarEntity.createdAt.desc()
                }
            )
            .fetch()
    }
}