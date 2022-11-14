package com.wafflestudio.seminar.core.user.repository

import com.querydsl.core.types.Projections
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.UserSeminar.domain.InstructorSeminarDTO
import com.wafflestudio.seminar.core.UserSeminar.domain.ParticipantSeminarDTO
import com.wafflestudio.seminar.core.UserSeminar.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.UserDTO
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.domain.profile.InstructorDTO
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantDTO
import com.wafflestudio.seminar.core.user.domain.profile.QInstructorProfile.instructorProfile
import com.wafflestudio.seminar.core.user.domain.profile.QParticipantProfile.participantProfile

interface UserCustomRepository {
    fun getUserProfile(userId: Long): UserEntity
    fun findWithProfiles(userIds: List<Long>): List<UserEntity>
}


class UserCustomRepositoryImpl(
    private val queryDslConfig: QueryDslConfig
): UserCustomRepository {
    private val queryFactory = queryDslConfig.jpaQueryFactory()
    
    override fun getUserProfile(userId: Long): UserEntity {
        return queryFactory
            .select(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.participantProfile).fetchJoin()
            .leftJoin(userEntity.instructorProfile).fetchJoin()
            .where(userEntity.id.eq(userId))
            .fetchOne()!!
    }

    override fun findWithProfiles(userIds: List<Long>): List<UserEntity> {
        return queryFactory
            .select(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.participantProfile).fetchJoin()
            .leftJoin(userEntity.instructorProfile).fetchJoin()
            .where(userEntity.id.`in`(userIds))
            .fetch()
    }
}