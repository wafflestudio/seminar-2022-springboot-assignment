package com.wafflestudio.seminar.core.profile.database

import org.springframework.data.jpa.repository.JpaRepository


interface ParticipantProfileRepository: JpaRepository<ParticipantProfileEntity?, Long> {
    fun findByUserId(userId: Long) : ParticipantProfileEntity?
    fun deleteByUserId(userId: Long)
}