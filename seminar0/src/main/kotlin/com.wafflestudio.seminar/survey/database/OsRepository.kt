package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsRepository {
    fun OfindById(id: Long): OperatingSystem?
    fun findByOsName(osName: String): OperatingSystem?
}
