package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.domain.User
import java.time.LocalDateTime

data class SurveyResponse(
    val id: Long? = null,
    val operatingSystem: OperatingSystem,
    val springExp: Int? = null,
    val rdbExp: Int? = null,
    val programmingExp: Int? = null,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
    val user_id: User? = null
)