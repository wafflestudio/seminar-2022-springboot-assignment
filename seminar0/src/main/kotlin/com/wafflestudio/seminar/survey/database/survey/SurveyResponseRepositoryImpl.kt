package com.wafflestudio.seminar.survey.database.survey

import com.wafflestudio.seminar.survey.database.MemoryDB
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.ErrorCode
import com.wafflestudio.seminar.survey.exception.SurveyNotFoundException
import org.springframework.stereotype.Repository

@Repository
class SurveyResponseRepositoryImpl(
    val memoryDB: MemoryDB,
): SurveyResponseRepository {
    
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        return memoryDB.getSurveyResponses()
            .firstOrNull { it.id == id } 
            ?: throw SurveyNotFoundException(ErrorCode.SURVEY_NOT_FOUND)
    }
    
}