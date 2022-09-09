package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface UserService {
    fun getAllSurvey(): List<SurveyResponse>
    fun getSurveyId(id: Long): SurveyResponse
}