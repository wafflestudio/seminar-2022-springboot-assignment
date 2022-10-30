package com.wafflestudio.seminar.core.user.dto

import java.time.LocalDateTime

data class UserRequest(
        val username: String = "",
        val password: String = "",
        val university: String? = null,
        val company: String? = null,
        val year: Int? = null,
)
