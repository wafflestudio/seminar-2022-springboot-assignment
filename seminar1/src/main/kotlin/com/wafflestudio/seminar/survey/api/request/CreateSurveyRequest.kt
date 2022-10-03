package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class CreateSurveyRequest(
    val major: String? = null,
    val os: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val grade: String? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)