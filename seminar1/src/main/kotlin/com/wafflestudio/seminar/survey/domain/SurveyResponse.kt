package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime

data class SurveyResponse(
    val id: Long,
    var userEntity: UserEntity? = null,
    val operatingSystem: OperatingSystem,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)