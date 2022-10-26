package com.wafflestudio.seminar.core.user.domain

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
)