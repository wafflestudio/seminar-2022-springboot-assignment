package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.exception.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.ErrorCode
import com.wafflestudio.seminar.survey.exception.SeminarException
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(private val memoryDB: MemoryDB) :
    SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        val findResults = memoryDB.getSurveyResponses().find{it.id == id}
        return findResults ?: throw SeminarException(ErrorCode.SURVEY_NOT_FOUND)
    }
} 