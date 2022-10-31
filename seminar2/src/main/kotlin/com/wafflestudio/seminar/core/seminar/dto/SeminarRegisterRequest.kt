package com.wafflestudio.seminar.core.seminar.dto

import javax.validation.constraints.NotNull

data class SeminarRegisterRequest(
        @field: NotNull(message = "Role should be given")
        val role: String? = null
)
