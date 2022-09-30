package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity

data class CreateUserRequest(
    val nickname: String,
    val email: String,
    val password: String,
    val survey: SurveyResponseEntity?
)