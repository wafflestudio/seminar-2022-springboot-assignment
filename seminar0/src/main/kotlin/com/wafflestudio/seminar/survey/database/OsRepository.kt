package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsRepository {
    fun findByName(name: String): OperatingSystem?
    fun findById(id: Long): OperatingSystem?
}