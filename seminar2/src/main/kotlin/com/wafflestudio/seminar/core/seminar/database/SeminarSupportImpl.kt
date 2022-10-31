package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.*
import org.springframework.stereotype.Component

@Component
class SeminarSupportImpl(
    private val queryFactory: JPAQueryFactory
) : SeminarSupport {
    override fun findSeminarByName(seminarName: String): List<SeminarEntity> {
        return queryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.seminarName.contains(seminarName))
            .fetch()
    }

    override fun findSeminarsByParticipantId(participantId: Long): List<SeminarForParticipantProfile> {
        return queryFactory
            .select(
                Projections.constructor(
                    SeminarForParticipantProfile::class.java,
                    seminarEntity.id,
                    seminarEntity.seminarName,
                    userSeminarEntity.createdAt,
                    userSeminarEntity.isActive,
                    userSeminarEntity.modifiedAt
                )
            ).from(seminarEntity)
            .rightJoin(userSeminarEntity)
            .on(userSeminarEntity.seminar.id.eq(seminarEntity.id))
            .where(
                userSeminarEntity.isInstructor.isFalse,
                userSeminarEntity.user.id.eq(participantId)
            )
            .fetch()
    }

    override fun findSeminarsByInstructorId(instructorId: Long): SeminarForInstructorProfile? {
        return queryFactory
            .select(
                Projections.constructor(
                    SeminarForInstructorProfile::class.java,
                    seminarEntity.id,
                    seminarEntity.seminarName,
                    userSeminarEntity.createdAt
                )
            )
            .from(seminarEntity)
            .rightJoin(userSeminarEntity)
            .on(userSeminarEntity.seminar.id.eq(seminarEntity.id))
            .where(
                userSeminarEntity.isInstructor.isTrue,
                userSeminarEntity.user.id.eq(instructorId)
            )
            .fetchOne()
    }
}