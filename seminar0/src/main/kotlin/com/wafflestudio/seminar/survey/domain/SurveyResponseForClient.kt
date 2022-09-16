package com.wafflestudio.seminar.survey.domain

import java.time.LocalDateTime

data class SurveyResponseForClient(
    val id: Long,
    val osName: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
//    val timestamp: LocalDateTime,
//    val backendReason: String? = null,
//    val waffleReason: String? = null,
//    val somethingToSay: String? = null
)
