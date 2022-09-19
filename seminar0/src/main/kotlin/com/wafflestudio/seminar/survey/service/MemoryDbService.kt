package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class MemoryDbService (
    private val surveyResponseRepository : SurveyResponseRepository,
    private val osRepository: OsRepository,
    ) : IService{
    override fun getAllSurveyResponses() : List<SurveyResponse>
    {
        return surveyResponseRepository.findAll()
    }
    override fun getSurveyResponseById(id : Long) : SurveyResponse
    {
        return surveyResponseRepository.findById(id)
    }
    override fun getAllOs() : List<OperatingSystem>
    {
        return osRepository.findAll()
    }
    override fun getOsById(osId : Long) : OperatingSystem
    {
        return osRepository.findById(osId)
    }
    override fun getOsByName(osName : String) : OperatingSystem
    {
        return osRepository.findByName(osName)
    }
}