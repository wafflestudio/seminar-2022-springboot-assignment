package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InstructorProfileRepository : JpaRepository<InstructorProfileEntity, Long>,
    ParticipantProfileRepositoryCustom {
    fun findByUserId(id: Long): InstructorProfileEntity?
}

interface InstructorProfileRepositoryCustom

@Component
class InstructorProfileRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : InstructorProfileRepositoryCustom