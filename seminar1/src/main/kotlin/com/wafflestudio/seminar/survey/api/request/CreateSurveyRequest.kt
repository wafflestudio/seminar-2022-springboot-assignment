package com.wafflestudio.seminar.survey.api.request

import javax.validation.constraints.NotBlank

data class CreateSurveyRequest(
    val major: String,
    @field: NotBlank(message = "운영체제를 입력해 주세요.")
    val operatingSystem: String,
    @field: NotBlank(message = "스프링 경험을 1과 5 사이의 정수로 입력해 주세요.")
    val springExp: Int,
    @field: NotBlank(message = "RDB 경험을 1과 5 사이의 정수로 입력해 주세요.")
    val rdbExp: Int,
    @field: NotBlank(message = "프로그래밍 경험을 1과 5 사이의 정수로 입력해 주세요.")
    val programmingExp: Int,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)