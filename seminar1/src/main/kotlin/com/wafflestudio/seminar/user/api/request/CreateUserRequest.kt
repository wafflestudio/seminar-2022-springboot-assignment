package com.wafflestudio.seminar.user.api.request

data class CreateUserRequest(
    val userID: String,
    val email: String,
    val password: String,
)