package com.wafflestudio.seminar.core.profile.database

import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InstructorProfileRepository : JpaRepository<InstructorProfileEntity, Long> {
    fun findByUserId(userId: Long) : InstructorProfileEntity?
    fun deleteByUserId(userId: Long)
}