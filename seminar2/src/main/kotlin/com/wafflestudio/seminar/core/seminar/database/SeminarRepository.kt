package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface SeminarRepository : JpaRepository<SeminarEntity, Long>,
    SeminarRepositoryCustom

interface SeminarRepositoryCustom

@Component
class SeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SeminarRepositoryCustom