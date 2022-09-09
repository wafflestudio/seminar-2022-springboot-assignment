package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val surveyResponseRepository: SurveyResponseRepository): UserService {
    override fun getAllSurvey(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun getSurveyId(id: Long): SurveyResponse {
        return surveyResponseRepository.findById(id)
    }
}