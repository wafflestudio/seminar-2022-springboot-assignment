package com.wafflestudio.seminar.survey.database

import org.springframework.data.jpa.repository.JpaRepository

interface OsRepository : JpaRepository<OperatingSystemEntity, Long> {
    fun findByOsName(name: String): OperatingSystemEntity?
}