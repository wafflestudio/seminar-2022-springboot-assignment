package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(private val memoryDB: MemoryDB) : SurveyResponseRepository {

    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        val surveyResponses = findAll()
        // TODO: Handle the case when not found (returning null)
        return surveyResponses.find { it.id == id }!!
    }
}