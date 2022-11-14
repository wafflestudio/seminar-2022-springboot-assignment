package com.wafflestudio.seminar.core.seminar.repository

import com.querydsl.core.types.OrderSpecifier
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.UserSeminar.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.*
import com.wafflestudio.seminar.core.seminar.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity


interface SeminarCustomRepository { 
    fun findSeminarsContainingWord(word: String?, order: String?): List<SeminarEntity>
}

class SeminarCustomRepositoryImpl(
    private val queryDslConfig: QueryDslConfig
) : SeminarCustomRepository {
    private val queryFactory = queryDslConfig.jpaQueryFactory()
    
    override fun findSeminarsContainingWord(word: String?, order: String?): List<SeminarEntity> {
        return queryFactory
            .select(seminarEntity)
            .from(seminarEntity)
            .leftJoin(userSeminarEntity)
                .on(userSeminarEntity.seminar.id.eq(seminarEntity.id)).fetchJoin()
            .where(seminarEntity.name.contains(word?:""))
            .orderBy(
                when (order) {
                    "earliest" -> seminarEntity.createdAt.asc()
                    else -> seminarEntity.createdAt.desc()
                } as OrderSpecifier<*>?
            )
            .fetch().toList()
    }
}