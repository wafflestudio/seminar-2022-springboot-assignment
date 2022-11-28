package com.wafflestudio.seminar.core.user.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class InstructingSeminar(
    val seminarId: Long,
    val name: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val joinedAt: LocalDateTime,
)