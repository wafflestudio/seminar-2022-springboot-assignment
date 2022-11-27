package com.wafflestudio.seminar.core.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.QInstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.QParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.userSeminar.database.QUserSeminarEntity
import org.springframework.stereotype.Repository

@Repository
class CustomUserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomUserRepository {
    override fun findByIdWithUserSeminar(id: Long): UserEntity? {
        val userEntity = QUserEntity.userEntity
        val userSeminarEntity = QUserSeminarEntity.userSeminarEntity
        val instructorProfileEntity = QInstructorProfileEntity.instructorProfileEntity
        val participantProfileEntity = QParticipantProfileEntity.participantProfileEntity
        return queryFactory
            .selectFrom(userEntity)
            .leftJoin(userEntity.userSeminars, userSeminarEntity)
            .fetchJoin()
            .leftJoin(userEntity.instructorProfile, instructorProfileEntity)
            .fetchJoin()
            .leftJoin(userEntity.participantProfile, participantProfileEntity)
            .fetchJoin()
            .where(userEntity.id.eq(id))
            .fetchOne()
    }
}