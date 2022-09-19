package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyDB: SurveyDB,
    private val exception: SeminarExceptionHandler,
): SurveyResponseRepository {
    
    private val db = surveyDB.getSurveyResponses()
    
    override fun findAll(): List<SurveyResponse> {
        return db
    }
    override fun findById(id: Long): SurveyResponse {
        for (data in db){
            if(data.id == id)
                return data
        }
        throw exception.SeminarException()
    }
}