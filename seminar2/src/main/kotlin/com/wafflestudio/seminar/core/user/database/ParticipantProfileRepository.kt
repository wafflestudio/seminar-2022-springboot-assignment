package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ParticipantProfileRepository : JpaRepository<ParticipantProfileEntity, Long>,
    ParticipantProfileRepositoryCustom {
    fun findByUserId(id: Long): ParticipantProfileEntity?
}

interface ParticipantProfileRepositoryCustom

@Component
class ParticipantProfileRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ParticipantProfileRepositoryCustom