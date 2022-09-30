package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import java.time.LocalDateTime

data class User(
    val nickname: String,
    val email: String,
    val password: String,
    val survey: SurveyResponseEntity?=null
)