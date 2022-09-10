package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service
import java.util.Collections.list
import java.util.ListResourceBundle

@Service
class SurveyService (
    private val surveyResponseRepository: SurveyResponseRepository,
        ){
    fun getAllResponses():List<SurveyResponse>{
        return surveyResponseRepository.findAll()
    }
    
    fun getResponseById(id:Long):SurveyResponse{
        return surveyResponseRepository.findById(id)
    }
}