package com.wafflestudio.seminar.survey.database

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OsRepository : JpaRepository<OperatingSystemEntity, Long> {
    fun findByOsName(name: String): OperatingSystemEntity?
}