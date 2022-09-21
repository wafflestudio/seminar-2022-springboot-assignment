package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.data.jpa.repository.JpaRepository

interface OsRepository : JpaRepository<OperatingSystem, Long> {
    fun findByOsName(name: String): OperatingSystem?
}