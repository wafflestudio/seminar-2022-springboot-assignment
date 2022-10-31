package com.wafflestudio.seminar.user.api.request

data class CreateUserRequest(
    val nickname: String,
    val email: String,
    val password: String
)