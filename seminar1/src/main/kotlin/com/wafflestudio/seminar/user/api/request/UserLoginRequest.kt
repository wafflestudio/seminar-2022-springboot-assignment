package com.wafflestudio.seminar.user.api.request

data class UserLoginRequest(
        val email: String,
        val password: String
)