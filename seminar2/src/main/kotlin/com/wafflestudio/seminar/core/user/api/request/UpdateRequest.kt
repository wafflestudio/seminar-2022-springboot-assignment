package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.PositiveOrZero

data class UpdateRequest(
    val username: String?,
    val password: String?,
    // case participant
    val university: String?,
    // case instructor
    val company: String?,
    @field: PositiveOrZero
    val year: Int?,
)