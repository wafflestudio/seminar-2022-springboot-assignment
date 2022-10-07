package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.SeminarException
import org.springframework.stereotype.Component

interface OsRepository {
    fun findAll():List<OperatingSystem>
    fun findById(id: Long): OperatingSystem?
    fun findByName(name: String): OperatingSystem?
}

