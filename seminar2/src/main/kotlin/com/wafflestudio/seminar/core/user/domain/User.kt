package com.wafflestudio.seminar.core.user.domain

data class User(
    val id: Long,
    val email: String,
    val name: String,
    val password: String,
    val role: Role
) {
    enum class Role {
        PARTICIPANT, INSTRUCTOR
    }
}