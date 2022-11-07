package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.SeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
}