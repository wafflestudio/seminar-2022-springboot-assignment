package com.wafflestudio.seminar.user.domain

data class SignUpResponse(
    val userId: Long,
    val name: String,
    val email: String,
)
