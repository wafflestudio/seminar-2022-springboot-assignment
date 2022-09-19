package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SurveyDB {
    fun getSurveyResponses(): List<SurveyResponse>
}