package com.wafflestudio.seminar.core.seminar.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Instructor(
    val userId: Long,
    val username: String,
    val email: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val joinedAt: LocalDateTime,
)