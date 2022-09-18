package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class SurveyResponseServiceImpl (
    private val surveyResponseRepository: SurveyResponseRepository,
): SurveyResponseService {
    override fun getAllSurveyResponses(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun getSurveyResponseById(id: Long): SurveyResponse {
        return surveyResponseRepository.findById(id) ?: throw NotFoundException("There is no survey response with the id.")
    }
}