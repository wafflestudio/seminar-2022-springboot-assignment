package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemorySurveyResponseRepository(
    private val db: MemoryDB
): SurveyResponseRepository{
    override fun findAll(): List<SurveyResponse> {
        return db.getSurveyResponses()
    }

    override fun findById(id: Long): Optional<SurveyResponse> {
        return db.getSurveyResponses().stream().filter { it.id == id }.findFirst()
    }
}