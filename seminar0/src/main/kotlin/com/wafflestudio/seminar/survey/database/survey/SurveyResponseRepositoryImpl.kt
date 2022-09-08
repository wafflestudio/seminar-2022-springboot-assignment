package com.wafflestudio.seminar.survey.database.survey

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Repository

@Repository
class SurveyResponseRepositoryImpl: SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): SurveyResponse {
        TODO("Not yet implemented")
    }
}