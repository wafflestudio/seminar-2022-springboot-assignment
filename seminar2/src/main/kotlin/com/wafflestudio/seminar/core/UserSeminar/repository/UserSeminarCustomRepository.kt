package com.wafflestudio.seminar.core.UserSeminar.repository

import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.UserSeminar.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType

interface UserSeminarCustomRepository {
    fun checkInstructingSeminars(userId: Long): Long
}

class UserSeminarCustomRepositoryImpl (
    private val queryDslConfig: QueryDslConfig
) : UserSeminarCustomRepository{
    private val queryFactory = queryDslConfig.jpaQueryFactory()
    
    override fun checkInstructingSeminars (userId: Long): Long {
        return queryFactory
            .select(userSeminarEntity.id.count())
            .from(userSeminarEntity)
            .where(
                userSeminarEntity.user.id.eq(userId)
                .and(userSeminarEntity.role.eq(RoleType.INSTRUCTOR))
                .and(userSeminarEntity.isActive.eq(true)))
            .fetchOne()!!
        }
}