package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository
): UserService {
    override fun getAllSurvey(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun getSurveyId(id: Long): SurveyResponse {
        return surveyResponseRepository.findById(id)
    }

    override fun getOSId(id: Long): OperatingSystem {
        return osRepository.findById(id)
    }
}