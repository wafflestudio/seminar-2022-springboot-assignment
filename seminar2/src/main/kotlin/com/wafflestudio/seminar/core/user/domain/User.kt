package com.wafflestudio.seminar.core.user.domain

data class User(
    val email: String,
    val username: String,
    val encodedPassword: String,
) {
    enum class Role {
        PARTICIPANT, INSTRUCTOR
    }
}