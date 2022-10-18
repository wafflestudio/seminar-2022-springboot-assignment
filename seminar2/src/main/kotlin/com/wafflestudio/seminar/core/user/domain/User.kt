package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.UserSeminarEntity

data class User(
    private val email: String,
    private val username: String,
    private val password: String,
    private val userSeminars: List<UserSeminar>
)