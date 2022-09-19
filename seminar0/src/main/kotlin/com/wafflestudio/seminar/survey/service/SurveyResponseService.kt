package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SurveyResponseService {
    fun findAll(): List<SurveyResponse>
    fun findById(id: Long): List<SurveyResponse>
}