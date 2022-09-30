package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import java.time.LocalDateTime

data class User(
    val id: Long,
    val nickname: String,
    val email: String,
    val password: String,
)