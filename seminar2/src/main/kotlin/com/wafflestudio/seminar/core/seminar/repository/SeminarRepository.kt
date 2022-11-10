package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
}