package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyResponseRepositoryImpl(private val memoryDB : MemoryDB): SurveyResponseRepository  {
    override fun findAll(): List<SurveyResponse> {
        return memoryDB.getSurveyResponses()
    }

    override fun findById(id: Long): SurveyResponse? {
        val list : List<SurveyResponse> =memoryDB.getSurveyResponses()
        list.forEach{
            if(it.id == id){
                return it
            }
        }
        return null
    }
}