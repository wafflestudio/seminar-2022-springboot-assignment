package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val major: String?,
    val spring_exp : Int?,
    val rdb_exp : Int?,
    val programming_exp : Int?,
    val os_name : String,
    val grade : String?,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)