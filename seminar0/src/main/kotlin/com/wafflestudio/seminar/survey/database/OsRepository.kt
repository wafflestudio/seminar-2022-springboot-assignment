package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsRepository {
    fun findAll(): List<OperatingSystem>
    fun findById(id: Long): OperatingSystem?
    fun findByName(name: String?): OperatingSystem?
}