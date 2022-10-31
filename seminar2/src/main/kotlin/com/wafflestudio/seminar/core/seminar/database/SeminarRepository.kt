package com.wafflestudio.seminar.core.seminar.database


import com.querydsl.core.types.Predicate
import com.querydsl.core.types.Projections
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.userseminar.database.QUserSeminarEntity.userSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun findByManagerId(managerId: Long): SeminarEntity?
}

interface SeminarRepositorySupport {
    fun getSeminarById(seminarId: Long): SeminarDto.SeminarProfileResponse
    fun getSeminars(name: String?, doEarliest: Boolean): MutableList<SeminarDto.SeminarProfileSimplifiedResponse>
}

@Component
class SeminarRepositorySupportImpl(
    private val queryDslConfig: QueryDslConfig
) : SeminarRepositorySupport {
    private val queryFactory = queryDslConfig.jpaQueryFactory()

    override fun getSeminarById(seminarId: Long): SeminarDto.SeminarProfileResponse {
        val instructors = queryFactory
            .select(
                Projections.constructor(
                    UserDto.SeminarInstructorProfileResponse::class.java,
                    userSeminarEntity.userEntity.id,
                    userSeminarEntity.userEntity.username,
                    userSeminarEntity.userEntity.email,
                    userSeminarEntity.joinedAt
                )
            )
            .from(seminarEntity)
            .rightJoin(userSeminarEntity)
            .on(
                userSeminarEntity.`in`(seminarEntity.userSeminarEntities),
                userSeminarEntity.role.eq("INSTRUCTOR")
            )
            .where(seminarEntity.id.eq(seminarId))
            .fetch()

        val participants = queryFactory
            .select(
                Projections.constructor(
                    UserDto.SeminarParticipantProfileResponse::class.java,
                    userSeminarEntity.userEntity.id,
                    userSeminarEntity.userEntity.username,
                    userSeminarEntity.userEntity.email,
                    userSeminarEntity.joinedAt,
                    userSeminarEntity.isActive,
                    userSeminarEntity.droppedAt
                )
            )
            .from(seminarEntity)
            .rightJoin(userSeminarEntity)
            .on(
                userSeminarEntity.`in`(seminarEntity.userSeminarEntities),
                userSeminarEntity.role.eq("PARTICIPANT")
            )
            .where(seminarEntity.id.eq(seminarId))
            .fetch()

        val result = queryFactory
            .select(
                Projections.constructor(
                    SeminarDto.SeminarProfileResponse::class.java,
                    seminarEntity.id,
                    seminarEntity.name,
                    seminarEntity.capacity,
                    seminarEntity.count,
                    seminarEntity.time,
                    seminarEntity.online
                )
            )
            .from(seminarEntity)
            .where(seminarEntity.id.eq(seminarId))
            .fetchFirst()

        result.instructors = instructors
        result.participants = participants
        return result
    }

    override fun getSeminars(
        name: String?,
        doEarliest: Boolean
    ): MutableList<SeminarDto.SeminarProfileSimplifiedResponse> {

        val result = queryFactory
            .select(
                Projections.constructor(
                    SeminarDto.SeminarProfileSimplifiedResponse::class.java,
                    seminarEntity.id,
                    seminarEntity.name,
                    seminarEntity.participantCount
                )
            )
            .from(seminarEntity)
            .where(
                when {
                    name.isNullOrBlank() -> seminarEntity.isNotNull
                    else -> seminarEntity.name.eq(name)
                } as Predicate?
            )
            .groupBy(seminarEntity.id)
            .orderBy(
                when {
                    doEarliest -> seminarEntity.createdAt.asc()
                    else -> seminarEntity.createdAt.desc()
                }
            )
            .fetch()


        result.forEach { response ->
            response.instructors = queryFactory
                .select(
                    Projections.constructor(
                        UserDto.SeminarInstructorProfileResponse::class.java,
                        userSeminarEntity.userEntity.id,
                        userSeminarEntity.userEntity.username,
                        userSeminarEntity.userEntity.email,
                        userSeminarEntity.joinedAt
                    )
                )
                .from(seminarEntity)
                .rightJoin(userSeminarEntity)
                .on(
                    userSeminarEntity.`in`(seminarEntity.userSeminarEntities),
                    userSeminarEntity.role.eq("INSTRUCTOR")
                )
                .where(seminarEntity.id.eq(response.id))
                .fetch()
        }

        return result
    }

}