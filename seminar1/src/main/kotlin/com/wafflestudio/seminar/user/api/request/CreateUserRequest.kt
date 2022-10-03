package com.wafflestudio.seminar.user.api.request

data class CreateUserRequest(
    val userName: String,
    val email: String,
    val password: String,
)