package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepositoryImpl
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyResponseService(
    private val surveyResponseRepository: SurveyResponseRepositoryImpl
) {
    fun findSurveys(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }
    
    fun findById(id: Long): SurveyResponse {
        val result: SurveyResponse? = surveyResponseRepository.findById(id)
        return result ?: throw IllegalArgumentException("No survey response with id $id")
    }
}