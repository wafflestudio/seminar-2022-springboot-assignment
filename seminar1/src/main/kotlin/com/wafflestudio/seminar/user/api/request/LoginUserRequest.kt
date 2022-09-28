package com.wafflestudio.seminar.user.api.request

data class LoginUserRequest(
    val email: String,
    var password: String
)
