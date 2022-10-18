package com.wafflestudio.seminar.core.user.domain

data class UserSeminar(
    private val user: User,
    private val seminar: Seminar,
    private val role: Role
) {
}