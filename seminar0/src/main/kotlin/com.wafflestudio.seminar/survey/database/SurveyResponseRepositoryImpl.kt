package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.constants.ErrorCode
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(private val memoryDB: MemoryDB) : SurveyResponseRepository {

    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        val surveyResponses = findAll()
        return surveyResponses.find { it.id == id } 
            ?: throw SeminarExceptionHandler().SeminarException(ErrorCode.SURVEY_ID_NOT_FOUND)
    }
}