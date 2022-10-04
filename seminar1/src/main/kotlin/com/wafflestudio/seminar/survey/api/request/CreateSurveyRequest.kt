package com.wafflestudio.seminar.survey.api.request

data class CreateSurveyRequest(
    val operatingSystem: String? = null,
    val springExp: Int = -1,
    val rdbExp: Int = -1,
    val programmingExp: Int = -1,
    val major: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)