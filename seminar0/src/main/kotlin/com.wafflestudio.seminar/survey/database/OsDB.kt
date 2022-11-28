package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsDB {
    fun getOperatingSystems(): List<OperatingSystem>
    fun getOperatingSystemById(id: Long): OperatingSystem
    fun getOperatingSystemByOSName(osName: String): OperatingSystem
}