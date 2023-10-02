package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank

data class RegisterSeminarRequest (
    @NotBlank
    val role: String
        )