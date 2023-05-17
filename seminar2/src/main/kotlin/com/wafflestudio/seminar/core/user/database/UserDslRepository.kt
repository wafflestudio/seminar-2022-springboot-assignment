package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.domain.QUserEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import org.springframework.stereotype.Component

@Component
class UserDslRepository(
        private val queryFactory: JPAQueryFactory
) {
    fun getUserProfile(userId: Long?) :UserEntity {
        return queryFactory.select(QUserEntity.userEntity)
                .from(QUserEntity.userEntity)
                .where(QUserEntity.userEntity.id.eq(userId)).fetchOne()
                ?: throw Seminar404("해당하는 유저가 없습니다")
    }
}