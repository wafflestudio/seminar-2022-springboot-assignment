package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructor
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipant
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.stereotype.Component

@Component
class UserSeminarDslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    fun findInstructorProjections(userSeminarIds: List<Long>): List<SeminarInstructor> {
        return queryFactory
            .select(
                Projections.constructor(
                    SeminarInstructor::class.java,
                    userEntity.id,
                    userEntity.username,
                    userEntity.email,
                    userSeminarEntity.createdAt
                )
            )
            .from(userSeminarEntity)
            .innerJoin(userEntity)
            .on(userSeminarEntity.userId.eq(userEntity.id))
            .where(userSeminarEntity.id.`in`(userSeminarIds))
            .fetch()
    }

    fun findParticipantProjections(userSeminarIds: List<Long>): List<SeminarParticipant> {
        return queryFactory
            .select(
                Projections.constructor(
                    SeminarParticipant::class.java,
                    userEntity.id,
                    userEntity.username,
                    userEntity.email,
                    userSeminarEntity.createdAt,
                    userSeminarEntity.isActive,
                    userSeminarEntity.droppedAt,
                )
            )
            .from(userSeminarEntity)
            .innerJoin(userEntity)
            .on(userSeminarEntity.userId.eq(userEntity.id))
            .where(userSeminarEntity.id.`in`(userSeminarIds))
            .fetch()
    }
}