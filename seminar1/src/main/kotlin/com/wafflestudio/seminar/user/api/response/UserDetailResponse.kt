package com.wafflestudio.seminar.user.api.response


import com.wafflestudio.seminar.survey.domain.SurveyResponse

class UserDetailResponse(
    val nickname: String,
    val email: String,
    val password: String,
    val survey: SurveyResponse?
)