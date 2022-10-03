package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val osName: String? = null,
    val springExp: Int? = null,
    val rdbExp: Int? = null,
    val programmingExp: Int? = null,
    val major: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)