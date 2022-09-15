package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(private val db: MemoryDB): SurveyResponseRepository {
    val surveyResponses = db.getSurveyResponses()
    
    override fun findAll(): List<SurveyResponse> {
        return surveyResponses
    }

    override fun findById(id: Long): SurveyResponse? {
        return surveyResponses.find {it.id == id}
    }
}