package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface UserService {
    fun getAllSurvey(): List<SurveyResponse>
    fun getSurveyOfId(id: Long): SurveyResponse
    fun getOSOfId(id: Long): OperatingSystem
    fun getOSOfName(name: String): OperatingSystem
}