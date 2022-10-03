package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponse>
    fun SfindById(id: Long): SurveyResponse?
}