package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.domain.SurveyResponseForClient

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponseForClient>
    fun findById(id: Long): SurveyResponseForClient
}