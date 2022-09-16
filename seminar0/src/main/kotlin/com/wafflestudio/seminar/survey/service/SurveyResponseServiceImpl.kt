package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyResponseServiceImpl(private val surveyResponseRepository: SurveyResponseRepository) :
    SurveyResponseService {
    override fun findAll(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun findBySurveyId(id: Long): List<SurveyResponse> {
        return surveyResponseRepository.findById(id)
    }
}
