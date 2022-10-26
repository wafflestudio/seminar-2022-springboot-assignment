package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long>, UserSeminarRepositoryCustom {
    fun findUserSeminarsBySeminarId(seminarId: Long) : List<UserSeminarEntity>
    fun findUserSeminarsByUserId(userId: Long) : List<UserSeminarEntity>
    fun findUserSeminarByUserIdAndSeminarId(userId: Long, seminarId: Long) : UserSeminarEntity?
    fun findAllByUserId(userId: Long) : List<UserSeminarEntity>
    fun findUserSeminarByUserIdAndIsParticipantAndIsActive(userId: Long, isParticipant: Boolean, isActive:Boolean) : UserSeminarEntity?
}

interface UserSeminarRepositoryCustom

@Component
class UserSeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserSeminarRepositoryCustom {
    
}