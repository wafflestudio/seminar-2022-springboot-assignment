package com.wafflestudio.seminar.user.domain

data class CreateUserResponse(
    val userId: Long,
    val name: String,
    val email: String
)