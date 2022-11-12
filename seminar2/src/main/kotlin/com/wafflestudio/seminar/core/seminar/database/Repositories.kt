package com.wafflestudio.seminar.core.seminar.database

import org.springframework.data.jpa.repository.JpaRepository
interface InstructorRepository : JpaRepository<InstructorEntity, Long>

interface ParticipantRepository : JpaRepository<ParticipantEntity, Long>
interface SeminarRepository : JpaRepository<SeminarEntity, Long>

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long> {
    fun findAllBySeminarId(seminarId: Long): List<UserSeminarEntity>
    fun findAllByUserId(userId: Long): List<UserSeminarEntity>
    
    fun findBySeminarIdAndUserId(seminarId: Long, userId: Long): UserSeminarEntity?
}

