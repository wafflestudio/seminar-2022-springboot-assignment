package com.wafflestudio.seminar.core.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.QSeminarEntity
import com.wafflestudio.seminar.core.user.database.SeminarEntity
import com.wafflestudio.seminar.core.user.domain.Seminar
import org.springframework.stereotype.Repository
import java.time.LocalTime

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