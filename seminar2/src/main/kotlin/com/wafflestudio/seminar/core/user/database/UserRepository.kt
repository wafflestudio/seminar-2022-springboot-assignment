package com.wafflestudio.seminar.core.user.database

import com.querydsl.core.annotations.QueryProjection
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.jpa.hibernate.HibernateQueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.api.response.UserProfile
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import com.wafflestudio.seminar.core.profile.database.QParticipantProfileEntity.participantProfileEntity
import com.wafflestudio.seminar.core.profile.database.QInstructorProfileEntity.instructorProfileEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity

import com.wafflestudio.seminar.core.user.type.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    fun findByEmail(email: String): UserEntity?
    fun findTopByOrderByIdDesc(): UserEntity?
}

interface UserRepositoryCustom {
    fun getAllUsersWithProfile(pageable: Pageable) : Page<UserQueryVO>
}


class UserQueryVO @QueryProjection constructor(
    val user: UserEntity? = null,
    val participantId: Long? = null,
    val instructorId: Long? = null,
)

@Component
class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun getAllUsersWithProfile(pageable: Pageable) : Page<UserQueryVO> {
        val result = queryFactory
            .select(
                QUserQueryVO(
                    userEntity,
                    participantProfileEntity.id,
                    instructorProfileEntity.id
                )
            )
            .from(userEntity)
            .leftJoin(participantProfileEntity)
            .on(userEntity.id.eq(participantProfileEntity.user.id))
            .leftJoin(instructorProfileEntity)
            .on(userEntity.id.eq(instructorProfileEntity.user.id))
            .orderBy(userEntity.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
//            .leftJoin(userEntity.userSeminars, userSeminarEntity).fetchJoin()
            .fetch()
        
        return PageImpl(result as List<UserQueryVO>, pageable, result.size.toLong())
    }
    
}