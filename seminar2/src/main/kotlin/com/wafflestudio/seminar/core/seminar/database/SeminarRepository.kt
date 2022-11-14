package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun findByHostId(hostId: Long): SeminarEntity?
}

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long>

@Component
class SeminarRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) {
    fun findAll(name: String?, order: String?, pageable: Pageable): List<SeminarEntity> {
        val ordering = when (order == "earliest") {
            true -> seminarEntity.createdAt.asc()
            false -> seminarEntity.createdAt.desc()
        }
        return queryFactory.selectDistinct(seminarEntity)
            .from(seminarEntity)
            .where(name?.let { seminarEntity.name.contains(name) })
            .orderBy(ordering)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
}

@Component
class UserSeminarRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) {
    fun find(userId: Long, seminarId: Long): UserSeminarEntity? {
        return queryFactory.selectFrom(userSeminarEntity)
            .where(
                userSeminarEntity.user.id.eq(userId),
                userSeminarEntity.seminar.id.eq(seminarId)
            )
            .fetchOne()
    }
}