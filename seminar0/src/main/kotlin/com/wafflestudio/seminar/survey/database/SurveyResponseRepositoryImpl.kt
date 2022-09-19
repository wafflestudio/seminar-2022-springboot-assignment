package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.DataNotFoundException
import com.wafflestudio.seminar.survey.exception.ErrorCode
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SurveyResponseRepositoryImpl (val memoryDB: MemoryDB): SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse>{
        return memoryDB.getSurveyResponses()
    }
    override fun findById(id: Long): SurveyResponse {
        return memoryDB.getSurveyResponses().first{it.id == id}?: throw DataNotFoundException(ErrorCode.DATA_NOT_FOUND)
    }
    
}