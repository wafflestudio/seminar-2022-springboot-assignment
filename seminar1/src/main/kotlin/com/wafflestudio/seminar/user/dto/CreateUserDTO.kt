package com.wafflestudio.seminar.user.dto

data class CreateUserDTO(
    val nickname: String,
    val email: String,
    val password: String,
)