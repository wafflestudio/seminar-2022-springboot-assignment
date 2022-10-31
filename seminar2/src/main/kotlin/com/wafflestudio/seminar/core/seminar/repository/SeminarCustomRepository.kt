package com.wafflestudio.seminar.core.seminar.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.wafflestudio.seminar.common.QueryDslConfig
import com.wafflestudio.seminar.core.UserSeminar.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.*
import com.wafflestudio.seminar.core.seminar.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType


interface SeminarCustomRepository { 
    fun findSeminarById(seminarId: Long): SeminarDTO
    fun findSeminarByName(name: String?, order: String?): List<SeminarGroupByDTO>?
    fun checkCapacity(seminarId: Long): Long
}

class SeminarCustomRepositoryImpl(
    private val queryDslConfig: QueryDslConfig
) : SeminarCustomRepository {
    val queryFactory = queryDslConfig.jpaQueryFactory()
    override fun findSeminarById(seminarId: Long): SeminarDTO {
        var dto = queryFactory
            .select(
                with(seminarEntity) {
                    Projections.constructor(
                        SeminarDTO::class.java,
                        this.id, this.name, this.capacity,
                        this.count, this.time, this.online
                    )
                }
            )
            .from(seminarEntity)
            .where(seminarEntity.id.eq(seminarId))
            .fetchOne()
        
        dto!!.apply { 
            this.instructors = queryFactory
                .select(
                    Projections.constructor(
                        SeminarInstructorDTO::class.java,
                        userEntity.id, userEntity.username, userEntity.email,
                        userSeminarEntity.joinedAt
                    )
                )
                .from(userSeminarEntity)
                .leftJoin(userSeminarEntity.user, userEntity)
                .where(userSeminarEntity.seminar.id.eq(seminarId)
                    .and(userSeminarEntity.role.eq(RoleType.INSTRUCTOR))
                    .and(userSeminarEntity.isActive.eq(true))) // 중간에 
                .fetch()

            this.participants = queryFactory
                .select(
                    Projections.constructor(
                        SeminarParticipantDTO::class.java,
                        userEntity.id, userEntity.username, userEntity.email,
                        userSeminarEntity.joinedAt,
                        userSeminarEntity.isActive,
                        userSeminarEntity.droppedAt?: null
                    )
                )
                .from(userSeminarEntity)
                .leftJoin(userSeminarEntity.user, userEntity)
                .where(userSeminarEntity.seminar.id.eq(seminarId)
                    .and(userSeminarEntity.role.eq(RoleType.PARTICIPANT)))
                .fetch()
        }
        
        return dto
    }


    override fun findSeminarByName(name: String?, order: String?): List<SeminarGroupByDTO>? {
        var dto = queryFactory
            .select(
                Projections.fields(
                    SeminarGroupByDTO::class.java,
                    seminarEntity.id,
                    seminarEntity.name,
                    CaseBuilder()
                        .`when`(userSeminarEntity.isActive.eq(true)
                            .and(userSeminarEntity.role.eq(RoleType.PARTICIPANT)))
                        .then(1L)
                        .otherwise(0L)       // 수강 유저가 없거나 (null), 활성상태 x (isActive.eq(false))
                        .sum()
                    .`as`("participantCount")
                )
            )
            .from(seminarEntity)
            .leftJoin(seminarEntity.userSeminarList, userSeminarEntity)
            .where(seminarEntity.name.contains(name?:""))
            .groupBy(seminarEntity.id)
            .orderBy(
                when (order) {
                    "earliest" -> seminarEntity.createdAt.asc()
                    else -> seminarEntity.createdAt.desc()
                } as OrderSpecifier<*>?
            )
            .fetch().toList()
        
        dto.forEach() {
            it.instructors = queryFactory
                .select(
                    Projections.constructor(
                        SeminarInstructorDTO::class.java,
                        userEntity.id, userEntity.username, userEntity.email,
                        userSeminarEntity.joinedAt
                    )
                )
                .from(userSeminarEntity)
                .leftJoin(userSeminarEntity.user, userEntity)
                .where(userSeminarEntity.seminar.id.eq(it.id)
                    .and(userSeminarEntity.role.eq(RoleType.INSTRUCTOR)))
                .fetch()
        }
        
        return dto
    }


    override fun checkCapacity(seminarId: Long): Long {
        return queryFactory
            .select(userSeminarEntity.user.id.count())
            .from(seminarEntity)
            .join(seminarEntity.userSeminarList, userSeminarEntity)
            .where(seminarEntity.id.eq(seminarId)
                .and(userSeminarEntity.role.eq(RoleType.PARTICIPANT))
                .and(userSeminarEntity.isActive.eq(true)))
            .fetchOne()!!
    }
}