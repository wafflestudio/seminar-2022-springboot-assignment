package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity as userSeminar
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity as user

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom

interface UserRepositoryCustom {
    fun findByEmail(email: String): UserEntity?
    fun findByIdWithAllOrNull(userId: Long): UserEntity?
}

@Component
class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun findByEmail(email: String): UserEntity? {
        return queryFactory
            .selectFrom(user)
            .where(user.email.eq(email))
            .fetchOne()
    }

    override fun findByIdWithAllOrNull(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(user)
            .from(user)
            .leftJoin(user.userSeminars, userSeminar)
            .fetchJoin()
            .leftJoin(user.instructorProfile)
            .fetchJoin()
            .leftJoin(user.participantProfile)
            .fetchJoin()
            .where(user.id.eq(userId))
            .fetchOne()
    }
}