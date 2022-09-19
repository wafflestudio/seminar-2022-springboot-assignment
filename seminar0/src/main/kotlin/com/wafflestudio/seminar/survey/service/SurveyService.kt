package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponse

interface SurveyService {
    fun findAll(): List<SurveyResponse>
    fun findById(id: Long): SurveyResponse
}