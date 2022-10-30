package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.Min

data class UserRequest(
    val university: String?,
    val company: String?,
    @field:Min(1, message = "year는 0 이상의 정수입니다")
    val year: Int?
)