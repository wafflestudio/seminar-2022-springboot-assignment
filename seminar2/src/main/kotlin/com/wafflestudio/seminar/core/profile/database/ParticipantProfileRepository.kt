package com.wafflestudio.seminar.core.profile.database

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

//fun<T, ID> CrudRepository<T, ID>.findByUserIdOrNull(id: ID) :T? = this.findByUserId(id).orElse(null)

interface ParticipantProfileRepository: JpaRepository<ParticipantProfileEntity?, Long> {
//    fun findByUserId(userId: Long) : ParticipantProfileEntity
    fun findByUserId(userId: Long) : ParticipantProfileEntity?
    fun deleteByUserId(userId: Long)
}