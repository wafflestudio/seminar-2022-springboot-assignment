package com.wafflestudio.seminar.survey.api.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CreateSurveyRequest(
    val major: String? = null,
    
    @field:NotBlank(message = "os 이름을 입력하세요")
    val os: String,

    @field:Min(1, message = "springExp는 1 이상 5 이하의 정수입니다")
    @field:Max(5, message = "springExp는 1 이상 5 이하의 정수입니다")
    val springExp: Int,

    @field:Min(1, message = "rdbExp는 1 이상 5 이하의 정수입니다")
    @field:Max(5, message = "rdbExp는 1 이상 5 이하의 정수입니다")
    val rdbExp: Int,

    @field:Min(1, message = "programmingExp는 1 이상 5 이하의 정수입니다")
    @field:Max(5, message = "programmingExp는 1 이상 5 이하의 정수입니다")
    val programmingExp: Int,
    
    val grade: String? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)