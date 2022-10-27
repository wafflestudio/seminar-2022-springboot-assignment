package com.wafflestudio.seminar.survey.api.request

import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class CreateSurveyRequest(
//    @field: NotBlank(message = "필수입력 사항입니다.")
    val operatingSystem: String,
//    @field: NotBlank(message = "필수입력 사항입니다.")
    val springExp: Int?=null,
//    @field: NotBlank(message = "필수입력 사항입니다.")
    val rdbExp: Int?=null,
//    @field: NotBlank(message = "필수입력 사항입니다.")
    val programmingExp: Int?=null,
    val major: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)