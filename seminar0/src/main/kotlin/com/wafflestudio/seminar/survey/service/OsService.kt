package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsService {
    fun findById(id: Long): OperatingSystem?
    fun findByName(name: String): OperatingSystem?
}