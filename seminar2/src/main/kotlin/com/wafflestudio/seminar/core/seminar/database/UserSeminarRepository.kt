package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserSeminarRepository :
    JpaRepository<UserSeminarEntity, Long>,
    UserSeminarRepositoryCustom

interface UserSeminarRepositoryCustom

@Component
class UserSeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserSeminarRepositoryCustom