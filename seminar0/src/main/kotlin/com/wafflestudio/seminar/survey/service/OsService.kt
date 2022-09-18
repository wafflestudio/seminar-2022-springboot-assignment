package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsService {
    fun getOsById(id: Long): OperatingSystem
    fun getOsByName(name: String): OperatingSystem
}