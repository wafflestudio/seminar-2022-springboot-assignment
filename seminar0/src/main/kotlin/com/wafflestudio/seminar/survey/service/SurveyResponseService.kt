package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SurveyResponseService {
    fun getAllSurveyResponses(): List<SurveyResponse>
    fun getSurveyResponseById(id: Long): SurveyResponse
}