package com.wafflestudio.seminar.survey.domain

import java.time.LocalDateTime

data class SurveyResponseShow(
    val id: Long,
    val operatingSystem: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)