package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsRepository {
    fun findById(id: Long): OperatingSystem
    fun findByName(osName: String): OperatingSystem
}