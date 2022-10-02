package com.wafflestudio.seminar.survey.api.response

import java.time.LocalDateTime

data class CreateSurveyResponse(
    val user: Long, 
    val operatingSystem: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)