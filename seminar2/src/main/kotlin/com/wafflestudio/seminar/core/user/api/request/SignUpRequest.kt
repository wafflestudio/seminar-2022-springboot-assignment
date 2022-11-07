package com.wafflestudio.seminar.core.user.api.request

data class SignUpRequest(
    val username: String?,
    val email: String?,
    val password: String?,
    val role: String?,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    val year: Long? = null,
)