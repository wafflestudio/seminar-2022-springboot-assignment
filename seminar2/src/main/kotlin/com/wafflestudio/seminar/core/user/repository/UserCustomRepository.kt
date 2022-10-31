package com.wafflestudio.seminar.core.user.repository

import com.querydsl.core.types.Projections
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.UserSeminar.domain.InstructorSeminarDTO
import com.wafflestudio.seminar.core.UserSeminar.domain.ParticipantSeminarDTO
import com.wafflestudio.seminar.core.UserSeminar.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.UserDTO
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.domain.profile.InstructorDTO
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantDTO
import com.wafflestudio.seminar.core.user.domain.profile.QInstructorProfile.instructorProfile
import com.wafflestudio.seminar.core.user.domain.profile.QParticipantProfile.participantProfile

interface UserCustomRepository {
    fun getUserProfile(userId: Long): UserDTO
}


class UserCustomRepositoryImpl(
    private val queryDslConfig: QueryDslConfig
): UserCustomRepository {
    val queryFactory = queryDslConfig.jpaQueryFactory()
    
    override fun getUserProfile(userId: Long): UserDTO {
        var dto: UserDTO? = queryFactory
            .select(
                Projections.constructor(
                    UserDTO::class.java,
                    userEntity.id, userEntity.username, userEntity.email,
                    userEntity.lastLogin,
                    userEntity.dateJoined,
                    // participant
                    with(participantProfile) {
                        Projections.constructor(
                            ParticipantDTO::class.java,
                            this.id, this.university, this.isRegistered
                        )
                    },
                    // instructor 
                    with(instructorProfile) {
                        Projections.constructor(
                            InstructorDTO::class.java,
                            this.id, this.company, this.year
                        )
                    }
                )
            )
            .from(userEntity)
            .leftJoin(userEntity.participantProfile, participantProfile)
            .leftJoin(userEntity.instructorProfile, instructorProfile)
            .where(userEntity.id.eq(userId))
            .fetchOne()
        
        // participant seminars 정보 - 리스트 넣기
        if(dto!!.participant!!.id == null) {
            dto.participant = null
        } else {
            dto.participant!!.seminars =
                queryFactory
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
                    .fetch()
        }
        
        
        // instructor instructingSeminars 정보 - 리스트 넣기
        if(dto.instructor!!.id == null) {
            dto.instructor = null
        } else {
            dto.instructor!!.instructingSeminars = 
                queryFactory
                    .select(
                        Projections.constructor(
                            InstructorSeminarDTO::class.java,
                            seminarEntity.id, seminarEntity.name,
                            userSeminarEntity.joinedAt
                        )
                    )
                    .from(userSeminarEntity)
                    .join(userSeminarEntity.seminar, seminarEntity)
                    .where(userSeminarEntity.user.id.eq(userId)
                        .and(userSeminarEntity.role.eq(RoleType.INSTRUCTOR))
                        .and(userSeminarEntity.isActive.eq(true)))
                    .fetchOne()
        }
        
        return dto
    }
}