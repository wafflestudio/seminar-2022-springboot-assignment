package com.wafflestudio.seminar.core.UserSeminar.repository

import com.querydsl.core.types.Projections
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.UserSeminar.domain.InstructorSeminarDTO
import com.wafflestudio.seminar.core.UserSeminar.domain.ParticipantSeminarDTO
import com.wafflestudio.seminar.core.UserSeminar.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructorDTO
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipantDTO
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType

interface UserSeminarCustomRepository {
    fun findInstructors(userId: Long): InstructorSeminarDTO?
    fun findParticipants(userId: Long): List<ParticipantSeminarDTO>
}

class UserSeminarCustomRepositoryImpl (
    private val queryDslConfig: QueryDslConfig
) : UserSeminarCustomRepository{
    private val queryFactory = queryDslConfig.jpaQueryFactory()

    override fun findInstructors(userId: Long): InstructorSeminarDTO? {
        println("---------------findInstructors---------------")
        return queryFactory
            .select(
                Projections.constructor(
                    InstructorSeminarDTO::class.java,
                    seminarEntity.id, seminarEntity.name,
                    userSeminarEntity.joinedAt, userSeminarEntity.isActive,
                )
            )
            .from(userSeminarEntity)
            .join(userSeminarEntity.seminar, seminarEntity)
            .where(userSeminarEntity.user.id.eq(userId)
                .and(userSeminarEntity.role.eq(RoleType.INSTRUCTOR)))
            .fetchOne()
    }

    override fun findParticipants(userId: Long): List<ParticipantSeminarDTO> {
        return queryFactory
            .select(
                Projections.constructor(
                    ParticipantSeminarDTO::class.java,
                    seminarEntity.id, seminarEntity.name,
                    userSeminarEntity.joinedAt,
                    userSeminarEntity.isActive,
                    userSeminarEntity.droppedAt?: null
                )
            )
            .from(userSeminarEntity)
            .join(userSeminarEntity.seminar, seminarEntity)
            .where(userSeminarEntity.user.id.eq(userId)
                .and(userSeminarEntity.role.eq(RoleType.PARTICIPANT)))
            .fetch() ?: listOf()
    }
}