package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity as seminar
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity as userSeminar
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity as user

interface SeminarRepository : JpaRepository<SeminarEntity, Long>,
    SeminarRepositoryCustom

interface SeminarRepositoryCustom {
    fun findContainingName(name: String): MutableList<SeminarEntity>

    fun findByIdWithAllOrNull(seminarId: Long): SeminarEntity?
}

@Component
class SeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SeminarRepositoryCustom {
    override fun findContainingName(name: String): MutableList<SeminarEntity> {
        return queryFactory.selectFrom(seminar).where(seminar.name.contains(name)).fetch()
    }

    override fun findByIdWithAllOrNull(seminarId: Long): SeminarEntity? {
        return queryFactory
            .selectDistinct(seminar)
            .from(seminar)
            .leftJoin(seminar.userSeminars, userSeminar)
            .fetchJoin()
            .leftJoin(userSeminar.user, user)
            .fetchJoin()
            .where(seminar.id.eq(seminarId))
            .fetchOne()
    }
}