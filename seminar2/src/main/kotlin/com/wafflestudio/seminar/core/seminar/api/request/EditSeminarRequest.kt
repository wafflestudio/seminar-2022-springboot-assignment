package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class EditSeminarRequest(
    @field:NotNull(message = "id is required")
    val id: Long?,
    val name: String?,
    @field:Positive(message = "capacity should be greater than zero")
    val capacity: Int?,
    @field:Positive(message = "count should be greater than zero")
    val count: Int?,
    val time: String?,
    val online: Boolean?
) {
}