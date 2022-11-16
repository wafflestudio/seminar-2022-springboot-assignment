package com.wafflestudio.seminar.core.seminar.api.request

import java.time.LocalTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class SeminarRequest(
    @field:NotBlank(message = "세미나 이름을 입력하세요")
    val name: String,
    @field:Min(1, message = "세미나 정원은 양의 정수입니다")
    val capacity: Int,
    @field:Min(1, message = "세미나 횟수는 양의 정수입니다")
    val count: Int,
    val time: LocalTime,
    val online: Boolean = true,
)