package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface InstructorProfileRepository : JpaRepository<InstructorProfileEntity, Long> {
}