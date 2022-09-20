package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(
    private val memoryDb: MemoryDB
): SurveyResponseRepository {

    override fun findAll(): List<SurveyResponse> {
        return memoryDb.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        return memoryDb.getSurveyResponses().find{surveyResponse: SurveyResponse -> surveyResponse.id == id} ?: throw SeminarExceptionHandler().OsIdException()
    }
}