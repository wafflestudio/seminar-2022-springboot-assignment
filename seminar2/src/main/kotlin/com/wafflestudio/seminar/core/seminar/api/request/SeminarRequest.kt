package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class SeminarRequest(
    @field:NotBlank @field:NotEmpty var name: String,
    @field:Min(1) var capacity: Int,
    @field:Min(1) var count: Int,
    var time: String,
    var online: Boolean = true
)
