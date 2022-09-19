package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.domain.SurveyResponseShow

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponseShow>
    fun findById(id: Long): SurveyResponseShow
}