package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyService(private val surveyRepo : SurveyResponseRepository) {
    
    fun searchAllSurveys(): List<SurveyResponse>{
        return surveyRepo.findAll()
    }
    fun searchById(id : Long) : SurveyResponse{
        return surveyRepo.findById(id)
    }
}