package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsDB {
    fun getOperatingSystems(): List<OperatingSystem>
}