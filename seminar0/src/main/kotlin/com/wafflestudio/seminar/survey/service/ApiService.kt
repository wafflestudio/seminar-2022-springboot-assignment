package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse

interface ApiService {
    fun findOSbyId(id:Long): OperatingSystem?

    fun findOSbyName(name:String): OperatingSystem?

    fun findSurveyResponsebyId(id:Long): SurveyResponse?

    fun findAllSurveyResponse():List<SurveyResponse>
}