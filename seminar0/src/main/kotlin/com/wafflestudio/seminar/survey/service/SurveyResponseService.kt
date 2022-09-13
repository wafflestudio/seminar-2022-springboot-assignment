package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.APIException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service


@Service
class SurveyResponseService(val surveyResponseRepository: SurveyResponseRepository) {
    
    fun getAllSurveyResponses(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }
    
    fun getSurveyResponseById(id: Long) : SurveyResponse {
        val surveyResponse : SurveyResponse? =  surveyResponseRepository.findById(id)
        if (surveyResponse != null) {
            return surveyResponse
        } else {
            throw APIException(HttpStatus.BAD_REQUEST, "BAD_REQUEST(400) - Invalid Survey Id: $id")
        }
    }
}