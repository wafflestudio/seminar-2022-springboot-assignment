package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom

interface UserRepositoryCustom {
    fun findByEmail(email: String): UserEntity?
//    fun getProfile(userId: Long): ProfileResponse?
}

@Component
class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    val user: QUserEntity = QUserEntity.userEntity
    
    override fun findByEmail(email: String): UserEntity? {
        return queryFactory
            .selectFrom(user)
            .where(user.email.eq(email))
            .fetchOne()
    }
}