package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.QInstructorProfileEntity.instructorProfileEntity
import com.wafflestudio.seminar.core.user.database.QParticipantProfileEntity.participantProfileEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}

interface ParticipantRepository : JpaRepository<ParticipantProfileEntity, Long>

interface InstructorRepository : JpaRepository<InstructorProfileEntity, Long>

@Component
class UserRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) {
    fun findParticipantById(userId: Long): ParticipantProfileEntity? {
        return queryFactory
            .selectFrom(participantProfileEntity)
            .where(participantProfileEntity.id.eq(userId))
            .fetchOne()
    }

    fun findInstructorById(userId: Long): InstructorProfileEntity? {
        return queryFactory
            .selectFrom(instructorProfileEntity)
            .where(instructorProfileEntity.id.eq(userId))
            .fetchOne()
    }
    
    fun lastLogInTime(userId: Long) {
        queryFactory.update(userEntity)
            .set(userEntity.modifiedAt, LocalDateTime.now())
            .where(userEntity.id.eq(userId))
            .execute()
    }
}