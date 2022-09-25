package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.domain.UserInfo
import java.time.LocalDateTime

data class SurveyResponse(
    val id: Long,
    val user: UserInfo? = null,
    val operatingSystem: OperatingSystem,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)