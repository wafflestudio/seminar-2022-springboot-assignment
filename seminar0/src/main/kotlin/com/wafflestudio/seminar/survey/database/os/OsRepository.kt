package com.wafflestudio.seminar.survey.database.os

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsRepository {
    fun findAll(): List<OperatingSystem>
    fun findById(id: Long): OperatingSystem
}