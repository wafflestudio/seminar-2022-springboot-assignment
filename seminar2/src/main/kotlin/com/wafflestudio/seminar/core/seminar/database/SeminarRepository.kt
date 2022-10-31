package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository: JpaRepository<SeminarEntity, Long> {
    fun findByName(name : String): List<SeminarEntity>
}