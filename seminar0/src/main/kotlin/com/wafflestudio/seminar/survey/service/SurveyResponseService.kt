package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepositoryImpl
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyResponseService(
    private val surveyResponseRepositoryImpl: SurveyResponseRepositoryImpl,
) {
    
    fun findAll(): List<SurveyResponse>{
        return surveyResponseRepositoryImpl.findAll()
    }
    
    fun findById(id: Long): SurveyResponse{
        return surveyResponseRepositoryImpl.findById(id)
    }
}