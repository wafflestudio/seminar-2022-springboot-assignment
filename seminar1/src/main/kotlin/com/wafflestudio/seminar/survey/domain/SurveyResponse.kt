package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.domain.UserEntity
import java.time.LocalDateTime
import javax.persistence.FetchType
import javax.persistence.ManyToOne

data class SurveyResponse(
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
    val somethingToSay: String? = null,
    val user_id: UserEntity? = null
)