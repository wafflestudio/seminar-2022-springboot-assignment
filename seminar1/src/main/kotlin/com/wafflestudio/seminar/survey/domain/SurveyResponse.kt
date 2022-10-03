package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.Column

data class SurveyResponse(
    val id: Long,
    val operatingSystem: OperatingSystem,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
    val user: UserEntity?
)
data class UserID(
    val user_id: String? = null
)