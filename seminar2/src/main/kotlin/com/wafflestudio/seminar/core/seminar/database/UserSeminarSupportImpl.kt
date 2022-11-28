package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.*
import org.springframework.stereotype.Component

@Component
class UserSeminarSupportImpl(
    private val queryFactory: JPAQueryFactory
) : UserSeminarSupport {

    override fun findInstructorsById(seminarId: Long): List<Instructor> {
        return queryFactory
            .select(
                Projections.constructor(
                    Instructor::class.java,
                    userEntity.id,
                    userEntity.username,
                    userEntity.email,
                    userSeminarEntity.createdAt
                )
            )
            .from(userEntity).rightJoin(userSeminarEntity)
            .on(userSeminarEntity.user.id.eq(userEntity.id))
            .where(
                userSeminarEntity.seminar.id.eq(seminarId),
                userSeminarEntity.isInstructor.isTrue
            ).fetch()

    }

    override fun findParticipantsById(seminarId: Long): List<Participant> {
        return queryFactory
            .select(
                Projections.constructor(
                    Participant::class.java,
                    userEntity.id,
                    userEntity.username,
                    userEntity.email,
                    userSeminarEntity.createdAt
                )
            )
            .from(userEntity).rightJoin(userSeminarEntity)
            .on(userSeminarEntity.user.id.eq(userEntity.id))
            .where(
                userSeminarEntity.seminar.id.eq(seminarId),
                userSeminarEntity.isInstructor.isFalse
            ).fetch()
    }

    override fun findActiveParticipantCountById(seminarId: Long): Long? {
        return queryFactory
            .select(userEntity.count())
            .from(userEntity)
            .rightJoin(userSeminarEntity)
            .on(userSeminarEntity.user.id.eq(userEntity.id))
            .where(
                userSeminarEntity.seminar.id.eq(seminarId),
                userSeminarEntity.isInstructor.isFalse,
                userSeminarEntity.isActive.isTrue
            ).fetchOne()
    }

    override fun findUserSeminarBySeminarIdAndUserId(seminarId: Long, userId: Long): UserSeminarEntity? {
        return queryFactory
            .selectFrom(userSeminarEntity)
            .where(userSeminarEntity.seminar.id.eq(seminarId))
            .rightJoin(userEntity)
            .on(userSeminarEntity.user.id.eq(userEntity.id))
            .where(userEntity.id.eq(userId))
            .fetchOne()
    }

}