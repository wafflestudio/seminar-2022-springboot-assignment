package com.wafflestudio.seminar.survey.api.request

data class CreateSurveyRequest(
    val os: String,
    val springExp: Int?,
    val rdbExp: Int?,
    val programmingExp: Int?,
    val major: String?,
    val grade: String?,
    val backendReason: String?,
    val waffleReason: String?,
    val somethingToSay: String?,
)