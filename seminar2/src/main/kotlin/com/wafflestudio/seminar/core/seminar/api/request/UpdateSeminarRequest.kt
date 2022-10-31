package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UpdateSeminarRequest(
    @field:NotNull val id: Long?,
    var name: String?,
    @field:Min(1) var capacity: Int?,
    @field:Min(1) var count: Int?,
    var time: String?,
    var online: Boolean? = true
)