package com.wafflestudio.seminar.core.seminar.api.request

import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class EditSeminarRequest(
    val name: String = "",
    @field: NotNull(message = "capacity값을 입력해주세요.")
    @field: Positive(message = "capacity값은 양수여야 합니다.")
    val capacity: Int,
    @field: NotNull(message = "count값을 입력해주세요.")
    @field: Positive(message = "count값은 양수여야 합니다.")
    val count: Int,
    val online: Boolean = true
)
