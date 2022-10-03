package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SurveyResponseDB {
    fun getSurveyResponses(): List<SurveyResponse>
    fun getSurveyResponseById(id: Long): SurveyResponse
}