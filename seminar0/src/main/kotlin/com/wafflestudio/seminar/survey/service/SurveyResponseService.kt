package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepositoryImpl
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class SurveyResponseService(
    private val surveyResponseRepository: SurveyResponseRepositoryImpl
) {
    fun findSurveys(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }
    
    fun findById(id: Long): SurveyResponse {
        val result: Optional<SurveyResponse> = surveyResponseRepository.findById(id)
        if (result.isEmpty) {
            throw IllegalArgumentException("SurveyResponse with id $id does not exist.")
        }
        
        return result.get()
    }
}