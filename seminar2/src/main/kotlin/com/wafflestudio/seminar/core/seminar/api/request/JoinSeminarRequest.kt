package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.Pattern

data class JoinSeminarRequest(
    @field:Pattern(regexp = "PARTICIPANT|INSTRUCTOR", message = "역할은 PARTICIPANT 또는 INSTRUCTOR 이어야 합니다.")
    val role: String,
)