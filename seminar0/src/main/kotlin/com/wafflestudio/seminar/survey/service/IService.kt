package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface IService {
    fun getAllSurveyResponses() : List<SurveyResponse>
    fun getSurveyResponseById(id : Long) : SurveyResponse
    fun getAllOs() : List<OperatingSystem>
    fun getOsById(osId : Long) : OperatingSystem
    fun getOsByName(osName : String) : OperatingSystem
}