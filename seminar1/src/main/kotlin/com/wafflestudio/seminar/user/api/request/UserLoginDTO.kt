package com.wafflestudio.seminar.user.api.request

data class UserLoginDTO(
        val email: String,
        val password: String
)