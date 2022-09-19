package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.domain.SurveyResponseShow
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyDB: SurveyDB,
    private val exception: SeminarExceptionHandler,
): SurveyResponseRepository {
    
    private val db = surveyDB.getSurveyResponses()
    
    fun toShow(initial: SurveyResponse): SurveyResponseShow{
        return SurveyResponseShow(
            id = initial.id,
            operatingSystem = initial.operatingSystem.osName,
            springExp = initial.springExp,
            rdbExp = initial.rdbExp,
            programmingExp = initial.programmingExp,
            major = initial.major,
            grade = initial.grade,
            backendReason = initial.backendReason,
            waffleReason = initial.waffleReason,
            somethingToSay = initial.somethingToSay
        )
    }
    
    override fun findAll(): List<SurveyResponseShow> {
        var showList: MutableList<SurveyResponseShow> = mutableListOf()
        for (data in db){
            showList.add(toShow(data))
        }
        return showList
    }
    
    override fun findById(id: Long): SurveyResponseShow {
        for (data in db){
            if(data.id == id)
                return toShow(data)
        }
        throw exception.SeminarException()
    }
}