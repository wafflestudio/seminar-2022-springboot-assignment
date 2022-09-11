package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import java.util.Optional

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponse>
    fun findById(id: Long): Optional<SurveyResponse>
}