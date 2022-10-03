package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val major: String?=null,
    val osName: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val grade: String?=null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)