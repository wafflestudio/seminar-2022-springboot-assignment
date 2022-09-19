package com.wafflestudio.seminar.survey.database

import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(
    val memoryDB: MemoryDB
): SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses().map(::SurveyResponse)
    }

    override fun findById(id: Long): SurveyResponse? {
        return memoryDB.getSurveyResponses()
            .map(::SurveyResponse)
            .associateBy { it.id }[id]
    }
}