package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface InstructorRepository: JpaRepository<ParticipantEntity, Long> {
    fun save(instructorEntity: InstructorEntity): InstructorEntity
}