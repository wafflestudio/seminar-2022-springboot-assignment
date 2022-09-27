package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.api.exception.ErrorCode
import com.wafflestudio.seminar.survey.api.exception.SeminarException
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.IllegalArgumentException

@Component
class SurveyResponseRepositoryImpl(private val memoryDB: MemoryDB) : SurveyResponseRepository {
    
    
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse {
        val surveys = memoryDB.getSurveyResponses()
        return  surveys.find { it.id == id } ?: throw SeminarException(ErrorCode.Survey_ID_NOT_FOUND,id.toString())
    }
}