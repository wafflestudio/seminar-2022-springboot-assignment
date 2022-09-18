package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(private val memoryDB: MemoryDB, private val seminarExceptionHandler: SeminarExceptionHandler) :
    SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): SurveyResponse {
        TODO("Not yet implemented")
    }
} 