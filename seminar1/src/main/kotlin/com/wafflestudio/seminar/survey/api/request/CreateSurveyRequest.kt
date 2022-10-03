package com.wafflestudio.seminar.survey.api.request

data class CreateSurveyRequest(
    val major: String?,
    val grade: String?,
    val os: String?,
    val springExp: Int?,
    val rdbExp: Int?,
    val programmingExp: Int?,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)