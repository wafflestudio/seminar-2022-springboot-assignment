package com.wafflestudio.seminar.survey.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class CreateSurveyRequest(
    @field:NotBlank(message = "해당 정보가 없습니다.")
    val os: String?,

    @field:NotNull(message = "해당 정보가 없습니다.")
    @field:Positive(message = "숙련도는 양수여야 합니다.")
    val springExp: Int?,

    @field:NotNull(message = "해당 정보가 없습니다.")
    @field:Positive(message = "숙련도는 양수여야 합니다.")
    val rdbExp: Int?,

    @field:NotNull(message = "해당 정보가 없습니다.")
    @field:Positive(message = "숙련도는 양수여야 합니다.")
    val programmingExp: Int?,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val major: String?,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val grade: String?,

    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)