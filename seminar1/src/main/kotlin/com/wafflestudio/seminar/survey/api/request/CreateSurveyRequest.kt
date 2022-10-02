package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity

import java.time.LocalDateTime

data class CreateSurveyRequest(
    val operatingSystem: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime= LocalDateTime.now(),
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)
{
    
}