package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

data class EditSeminarRequest(
    @field:NotEmpty
    val id: Long,
    @field:NotBlank
    val name: String,
    @field:NotEmpty
    @field:Positive
    val capacity: Int,
    @field:NotEmpty
    @field:Positive
    val count: Int,
    @field:NotBlank
    val time: String,
    val online: Boolean = true
) {
}