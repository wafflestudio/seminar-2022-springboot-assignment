package com.wafflestudio.seminar.core.user.domain

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val role: Role
) {
    enum class Role {
        PARTICIPANT, INSTRUCTOR
    }
}