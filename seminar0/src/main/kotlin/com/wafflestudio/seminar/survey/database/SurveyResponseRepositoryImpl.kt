package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(
    val memoryDB: MemoryDB
): SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        return memoryDB.getSurveyResponses()
            .associateBy { it.id }[id]
            ?: throw NoSuchElementException()
    }
}