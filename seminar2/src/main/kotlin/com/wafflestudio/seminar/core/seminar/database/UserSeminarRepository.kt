package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long>, UserSeminarRepositoryCustom {
    @Query("select DISTINCT us from UserSeminarEntity us join fetch us.seminar s join fetch us.user u where s.id = :seminarId")
    fun findUserSeminarsBySeminarId(@Param("seminarId") seminarId: Long) : List<UserSeminarEntity>
    
    @Query("select us from UserSeminarEntity us join fetch us.seminar s join fetch us.user u where s.id = :seminarId and u.id = :userId")
    fun findUserSeminarByUserIdAndSeminarId(@Param("userId") userId: Long, @Param("seminarId") seminarId: Long) : UserSeminarEntity?

    @Query("select DISTINCT us from UserSeminarEntity us join fetch us.seminar s join fetch us.user u where u.id = :userId")
    fun findAllByUserId(@Param("userId") userId: Long) : List<UserSeminarEntity>

    fun findUserSeminarByUserIdAndIsParticipantAndIsActive(userId: Long, isParticipant: Boolean, isActive:Boolean) : UserSeminarEntity?
    
    // TODO: It doesn't work 
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from UserSeminarEntity us where us.seminar.id = :seminarId")
    fun deleteAllBySeminarId(@Param("seminarId") seminarId: Long) : Int
}

interface UserSeminarRepositoryCustom

@Component
class UserSeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserSeminarRepositoryCustom {
    
}