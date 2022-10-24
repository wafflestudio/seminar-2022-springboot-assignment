package com.wafflestudio.seminar.survey.domain

import com.wafflestudio.seminar.user.domain.UserEntity
import com.wafflestudio.seminar.user.dto.ResponseUserDTO
import java.time.LocalDateTime
import javax.persistence.FetchType
import javax.persistence.ManyToOne

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
    val user: ResponseUserDTO? = null
)