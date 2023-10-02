package com.wafflestudio.seminar.core.user.database.profile

import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantProfileRepository : JpaRepository<ParticipantProfileEntity, Long>{
}