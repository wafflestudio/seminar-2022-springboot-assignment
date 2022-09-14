package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseDB
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class SurveyResponseRepositoryImpl(
    private val dataBase: SurveyResponseDB
): SurveyResponseRepository {
    
    override fun findAll(): List<SurveyResponse> {
        return dataBase.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        TODO("Not yet implemented")
    }
}