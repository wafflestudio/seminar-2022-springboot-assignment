package com.wafflestudio.seminar.core.user.database


import com.querydsl.core.types.Projections
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.user.database.QInstructorProfileEntity.instructorProfileEntity
import com.wafflestudio.seminar.core.user.database.QParticipantProfileEntity.participantProfileEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import com.wafflestudio.seminar.core.userseminar.database.QUserSeminarEntity.userSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long> {
    
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}

interface UserRepositorySupport {
    fun getProfile(userId: Long): UserDto.UserProfileResponse
    fun findAllWithUserIds(userIds: List<Long>): List<UserEntity>
}

@Component
class UserRepositorySupportImpl(
    private val queryDslConfig: QueryDslConfig
) : UserRepositorySupport {
    private val queryFactory = queryDslConfig.jpaQueryFactory()

    override fun findAllWithUserIds(userIds: List<Long>): List<UserEntity> {
        return queryFactory
            .select(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.instructorProfileEntity)
            .fetchJoin()
            .leftJoin(userEntity.participantProfileEntity)
            .fetchJoin()
            .where(userEntity.id.`in`(userIds))
            .fetch()
    }
    
    override fun getProfile(userId: Long): UserDto.UserProfileResponse {
        val seminars = queryFactory
            .select(
                Projections.constructor(
                    SeminarDto.SeminarResponse::class.java,
                    userSeminarEntity.seminarEntity.id,
                    userSeminarEntity.seminarEntity.name,
                    userSeminarEntity.joinedAt,
                    userSeminarEntity.isActive,
                    userSeminarEntity.droppedAt
                )
            )
            .from(userEntity)
            .rightJoin(userSeminarEntity)
            .on(userSeminarEntity.`in`(userEntity.userSeminarEntities), userSeminarEntity.role.eq(UserDto.Role.PARTICIPANT))
            .where(userEntity.id.eq(userId))
            .fetch()

        val instructingSeminars = queryFactory
            .select(
                Projections.constructor(
                    SeminarDto.InstructingSeminarResponse::class.java,
                    userSeminarEntity.seminarEntity.id,
                    userSeminarEntity.seminarEntity.name,
                    userSeminarEntity.joinedAt
                )
            )
            .from(userEntity)
            .rightJoin(userSeminarEntity)
            .on(userSeminarEntity.`in`(userEntity.userSeminarEntities), userSeminarEntity.role.eq(UserDto.Role.INSTRUCTOR))
            .where(userEntity.id.eq(userId))
            .fetch()

        val result = queryFactory
            .select(
                Projections.constructor(
                    UserDto.UserProfileResponse::class.java,
                    userEntity.id,
                    userEntity.username,
                    userEntity.email,
                    userEntity.modifiedAt.`as`("lastLogin"),
                    userEntity.createdAt.`as`("dateJoined"),
                    Projections.constructor(
                        UserDto.ParticipantProfileResponse::class.java,
                        participantProfileEntity.id,
                        participantProfileEntity.university,
                        participantProfileEntity.isRegistered,
                    ),
                    Projections.constructor(
                        UserDto.InstructorProfileResponse::class.java,
                        instructorProfileEntity.id,
                        instructorProfileEntity.company,
                        instructorProfileEntity.year,
                    )
                )
            )
            .from(userEntity)
            .leftJoin(userEntity.participantProfileEntity, participantProfileEntity)
            .leftJoin(userEntity.instructorProfileEntity, instructorProfileEntity)
            .where(userEntity.id.eq(userId))
            .fetchFirst()

        if (result.participant!!.id == 0L) {
            result.participant = null
        }
        if (result.instructor!!.id == 0L) {
            result.instructor = null
        }
        result.participant?.seminars = seminars
        result.instructor?.instructingSeminars = when {
            instructingSeminars.isEmpty() -> null
            else -> instructingSeminars
        }
        return result
    }
}