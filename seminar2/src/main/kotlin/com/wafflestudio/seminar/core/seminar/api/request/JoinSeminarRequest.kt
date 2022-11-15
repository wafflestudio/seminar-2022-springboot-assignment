package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.Pattern

data class JoinSeminarRequest(
    @field: Pattern(regexp = "Instructor|Participant", message = "올바른 role값을 입력해주세요.")
    val role: String
)
