package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.*

data class SeminarRequest (
    @field:NotBlank(message = "세미나 이름은 필수 입력값입니다.")
    val name: String?,
    @field:NotNull(message="세미나 정원은 필수 입력값입니다.")
    @field:Positive(message="세미나 정원은 양수로만 설정할 수 있습니다.")
    val capacity: Long?,         // 세미나 정원
    @field:NotNull(message="세미나 횟수는 필수 입력값입니다.")
    @field:Positive(message="세미나 횟수는 양수로만 설정할 수 있습니다.")
    val count: Long?,            // 세미나 횟수
    @field:NotBlank(message="세미나 정기 시간은 필수 입력값입니다.")
    @field:Pattern(
        regexp="[0-9]{2}:[0-9]{2}",
        message="HH:MM 양식에 맞춰 시간을 입력해주세요."
    )
    val time: String?,          // "hh:mm"으로 받음
    val online: Boolean?= true  // 온라인 수강 여부
)