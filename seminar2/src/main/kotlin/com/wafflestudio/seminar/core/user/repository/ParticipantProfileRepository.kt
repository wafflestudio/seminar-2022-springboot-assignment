package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantProfileRepository : JpaRepository<ParticipantProfileEntity, Long> {
}