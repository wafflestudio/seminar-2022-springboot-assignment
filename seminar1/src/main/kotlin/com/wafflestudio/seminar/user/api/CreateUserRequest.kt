package com.wafflestudio.seminar.user.api

data class CreateUserRequest(
    val nickname: String,
    val email: String,
    val password: String,
)