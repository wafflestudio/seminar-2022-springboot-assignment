package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface UserService {
    fun showAll(): List<SurveyResponse>
    fun showSurveyById(id: Long): SurveyResponse
    fun showOsById(id: Long): OperatingSystem
    fun showOsByOsName(osName: String): OperatingSystem
}