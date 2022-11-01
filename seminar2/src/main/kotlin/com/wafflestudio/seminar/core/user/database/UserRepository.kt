package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    fun findByEmail(email: String): UserEntity?
}

interface UserRepositoryCustom {
    fun findAllWithProfiles(userIds: List<Long>): List<UserEntity>
}

@Component
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun findAllWithProfiles(userIds: List<Long>): List<UserEntity> {
        return queryFactory
            .select(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.instructorProfile)
            .fetchJoin()
            .leftJoin(userEntity.participantProfile)
            .fetchJoin()
            .where(userEntity.id.`in`(userIds))
            .fetch()
    }
}