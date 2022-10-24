package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import java.time.LocalDateTime

data class SurveyResponse(
    val id: Long,
    val operatingSystem: OperatingSystem,
    val user: UserDetailResponse?,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String?,
    val grade: String?,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)