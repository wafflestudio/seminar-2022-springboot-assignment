package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class CreateSeminarRequest(
    @field:NotBlank(message = "name is required")
    val name: String?,
    @field:NotNull(message = "capacity is required")
    @field:Positive(message = "capacity should be greater than zero")
    val capacity: Int?,
    @field:NotNull(message = "count is required")
    @field:Positive(message = "count should be greater than zero")
    val count: Int?,
    @field:NotBlank(message = "time is required")
    val time: String?,
    val online: Boolean = true
) {
}