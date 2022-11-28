package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

interface UserRepository : JpaRepository<UserEntity, Long>, UserSupport {
    fun findByEmail(email: String): UserEntity?
}

interface UserSupport {
    fun findByEmailWithAllProfile(email: String): UserEntity?
    fun findInstructor(userId: Long): UserEntity?
    fun findUserWithAllInfo(userId: Long): UserEntity?
}

@Repository
class UserSupportImpl(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(UserEntity::class.java), UserSupport {

    override fun findByEmailWithAllProfile(email: String): UserEntity? {
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(userEntity.email.eq(email))
            .leftJoin(userEntity.instructorProfile)
            .fetchJoin()
            .leftJoin(userEntity.participantProfile)
            .fetchJoin()
            .fetchOne()
    }

    override fun findInstructor(userId: Long): UserEntity? {
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(userEntity.id.eq(userId))
            .innerJoin(userEntity.instructorProfile)
            .fetchJoin()
            .leftJoin(userEntity.instructingSeminars)
            .fetchJoin()
            .fetchOne()
    }

    override fun findUserWithAllInfo(userId: Long): UserEntity? {
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(userEntity.id.eq(userId))
            .leftJoin(userEntity.participantProfile)
            .fetchJoin()
            .leftJoin(userEntity.participatingSeminars)
            .fetchJoin()
            .leftJoin(userEntity.instructorProfile)
            .fetchJoin()
            .leftJoin(userEntity.instructingSeminars)
            .fetchJoin()
            .fetchOne()
    }
}