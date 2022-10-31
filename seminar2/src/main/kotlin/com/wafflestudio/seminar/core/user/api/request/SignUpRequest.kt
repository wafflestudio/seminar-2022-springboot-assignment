package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.common.Role

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val role : String
)