package com.wafflestudio.seminar.core.seminar.database

import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantSeminarTableRepository : JpaRepository<ParticipantSeminarTableEntity, Long>

interface InstructorSeminarTableRepository : JpaRepository<InstructorSeminarTableEntity, Long>