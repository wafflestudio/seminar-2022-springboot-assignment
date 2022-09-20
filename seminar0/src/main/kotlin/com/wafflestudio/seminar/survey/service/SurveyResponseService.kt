package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.survey.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyResponseService(
    val surveyResponseRepository: SurveyResponseRepository
) {
    fun findAll(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    fun findById(id: Long): SurveyResponse {
        return surveyResponseRepository.findById(id)
    }

}