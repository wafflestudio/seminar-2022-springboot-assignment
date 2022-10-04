package com.wafflestudio.seminar.user.api

data class LoginRequest (
    val email: String,
    val password: String
)