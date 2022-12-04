package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.stereotype.Component

@Component
class SeminarDslRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun getList(name: String?, isAscending: Boolean): List<SeminarEntity> {
        val ordering = when (isAscending) {
            true -> seminarEntity.createdAt.asc()
            else -> seminarEntity.createdAt.desc()
        }

        return queryFactory
            .selectDistinct(seminarEntity)
            .from(seminarEntity)
            .leftJoin(userSeminarEntity)
            .on(userSeminarEntity.seminar.eq(seminarEntity))
            .fetchJoin()
            .leftJoin(userSeminarEntity)
            .on(userSeminarEntity.user.eq(userEntity))
            .fetchJoin()
            .where(name?.let { seminarEntity.name.contains(name) })
            .orderBy(ordering)
            .fetch()
    }
}