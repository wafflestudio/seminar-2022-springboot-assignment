package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}

interface ParticipantRepository : JpaRepository<ParticipantProfileEntity, Long>

interface InstructorRepository : JpaRepository<InstructorProfileEntity, Long>

//@Component
//class UserRepositorySupport(
//    private val queryFactory: JPAQueryFactory,
//) {
//    fun findByIdOrNull(userId: Long): UserEntity? {
//        return queryFactory.selectFrom(userEntity)
//            .where(userEntity.id.eq(userId))
//            .fetchOne()
//    }
//}