package com.wafflestudio.seminar.survey.api.request

import javax.validation.constraints.NotBlank

data class CreateSurveyRequest(
    val major: String,
    @field: NotBlank(message = "운영체제를 입력해 주세요.")
    val operatingSystem: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)