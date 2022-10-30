package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository: JpaRepository<ParticipantEntity, Long> {
    fun save(participantEntity: ParticipantEntity): ParticipantEntity
}