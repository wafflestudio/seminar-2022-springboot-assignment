package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.domain.Role

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: Role,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    val year: Int? = null
)