package com.wafflestudio.seminar.core.seminar.database

import org.springframework.data.jpa.repository.JpaRepository

interface InstructorRepository: JpaRepository<InstructorEntity, Long> {
    fun findByUserIdIn(userIds: List<Long>): List<InstructorEntity>
}

interface ParticipantRepository: JpaRepository<ParticipantEntity, Long>  {
    fun findByUserIdIn(userIds: List<Long>): List<ParticipantEntity>    
}

interface SeminarRepository: JpaRepository<SeminarEntity, Long> 

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long> {
    fun findAllBySeminarId(seminarId: Long): List<UserSeminarEntity>
}