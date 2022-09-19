package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(
    private val memoryDB: MemoryDB
) : SurveyResponseRepository
{
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        val responses = memoryDB.getSurveyResponses()
        return responses.find{r -> r.id == id} ?: throw SeminarExceptionHandler().NoSuchIdException()
    }
}