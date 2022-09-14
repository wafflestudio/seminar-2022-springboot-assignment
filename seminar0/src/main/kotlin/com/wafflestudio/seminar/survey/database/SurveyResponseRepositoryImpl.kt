package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Repository

@Repository
class SurveyResponseRepositoryImpl(
    private val db: MemoryDB
): SurveyResponseRepository{
    override fun findAll(): List<SurveyResponse> {
        return db.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse? {
        return db.getSurveyResponses().associateBy { it.id }[id]
    }
}