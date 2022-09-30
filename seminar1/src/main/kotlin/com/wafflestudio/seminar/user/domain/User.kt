package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity

data class User(
    val nickname: String,
    val email: String,
    val password: String,
    val survey: SurveyResponseEntity?
)