package com.wafflestudio.seminar.user.domain

data class UserResponse(
    val userId: Long,
    val name: String,
    val email: String
)