package com.wafflestudio.seminar.survey.database.survey

import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponse>
    fun findById(id: Long): SurveyResponse
}