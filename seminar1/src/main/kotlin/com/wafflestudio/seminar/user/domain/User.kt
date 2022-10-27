package com.wafflestudio.seminar.user.domain

data class User(
    val nickname: String,
    val email: String,
    val encodedPassword: String,
    val userId: Long,
)