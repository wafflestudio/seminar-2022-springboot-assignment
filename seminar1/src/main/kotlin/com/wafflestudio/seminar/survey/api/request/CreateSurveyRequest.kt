package com.wafflestudio.seminar.survey.api.request


import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val osName: String? = null,
    val springExp: Int? = null,
    val rdbExp: Int? = null,
    val programmingExp: Int? = null,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
    val user_id: Long
)