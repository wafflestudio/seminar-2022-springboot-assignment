package com.wafflestudio.seminar.user.api.request

data class SignInUserRequest(
    val email: String,
    val password: String
)