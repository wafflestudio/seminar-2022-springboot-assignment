package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import java.time.LocalDateTime

data class UserSurveyResponse(
        val userId: Long,
        val name: String,
        val email: String,
        val survey: UserSurvey
)

data class UserSurvey(
        val id: Long,
        val operatingSystem: OperatingSystem,
        val springExp: Int,
        val rdbExp: Int,
        val programmingExp: Int,
        val major: String,
        val grade: String,
        val timestamp: LocalDateTime,
        val backendReason: String? = null,
        val waffleReason: String? = null,
        val somethingToSay: String? = null
)