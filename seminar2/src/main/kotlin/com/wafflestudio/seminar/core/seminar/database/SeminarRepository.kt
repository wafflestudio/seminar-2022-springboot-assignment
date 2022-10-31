package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun findByHostId(hostId: Long): SeminarEntity?
}

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long>

@Component
class UserSeminarRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) {
    fun find(userId: Long, seminarId: Long): UserSeminarEntity? {
        return queryFactory.selectFrom(userSeminarEntity)
            .where(userSeminarEntity.user.id.eq(userId),
                userSeminarEntity.seminar.id.eq(seminarId))
            .fetchOne()
    }
}