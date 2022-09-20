package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponse>
    fun findById(id: Long): SurveyResponse
}

@Component
class DefaultSurveyRepository(
    db: MemoryDB
): SurveyResponseRepository {
    val surveyList = db.getSurveyResponses()
    override fun findAll(): List<SurveyResponse> {
        return surveyList
    }

    override fun findById(id: Long): SurveyResponse {
        return surveyList.filter {
            it.id == id
        }.first()
    }
}