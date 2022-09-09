package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface SearchService {
    fun getOsById(id: Long):OperatingSystem
    fun getOsByName(name: String): OperatingSystem
    fun getAllSurveyResponses(): List<SurveyResponse>
    fun getSurveyResponseById(id: Long): SurveyResponse
}