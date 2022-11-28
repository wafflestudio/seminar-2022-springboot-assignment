package com.wafflestudio.seminar.survey.api.request


data class CreateSurveyRequest(
    val osName: String? = null,
    val springExp: Int? = null,
    val rdbExp: Int? = null,
    val programmingExp: Int? = null,
    val major: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)