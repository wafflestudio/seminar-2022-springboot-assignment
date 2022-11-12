package com.wafflestudio.seminar.core.userSeminar.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity
import com.wafflestudio.seminar.core.userSeminar.database.QUserSeminarEntity
import com.wafflestudio.seminar.core.userSeminar.database.UserSeminarEntity
import org.springframework.stereotype.Repository

@Repository
class CustomUserSeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomUserSeminarRepository {

    override fun findByUserIdAndSeminarId(userId: Long, seminarId: Long): UserSeminarEntity? {
        val userSeminarEntity = QUserSeminarEntity.userSeminarEntity
        val userEntity = QUserEntity.userEntity
        val seminarEntity = QSeminarEntity.seminarEntity
        return queryFactory
            .selectFrom(userSeminarEntity)
            .join(userSeminarEntity.user, userEntity)
            .join(userSeminarEntity.seminar, seminarEntity)
            .where(userEntity.id.eq(userId))
            .where(seminarEntity.id.eq(seminarId))
            .fetchOne()
    }
}