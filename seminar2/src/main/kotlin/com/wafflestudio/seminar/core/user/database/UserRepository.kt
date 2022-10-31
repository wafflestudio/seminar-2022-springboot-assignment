package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom{
    fun findByEmail(email : String) : UserEntity?
    fun findByName(name : String):UserEntity?
}
interface UserRepositoryCustom
@Component
class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): UserRepositoryCustom{
    fun anything(): List<UserEntity> {
//        return queryFactory
////            .select(userEntity)
////            .from(userEntity)
////            .innerJoin(lectureEntity)
////            .on(lectureEntity.instructor.eq(userEntity))
////            .where(lectureEntity.title.eq("어쩔티비"))
////            .fetch()
        return emptyList()
    }
}