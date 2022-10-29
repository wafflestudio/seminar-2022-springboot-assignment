package com.wafflestudio.seminar.user.api.request

data class LoginRequest(
    val email : String,
    val password : String
)