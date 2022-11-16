package com.wafflestudio.seminar.core.user.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

data class User(
    val id: Long,
    val email: String,
    val username: String,
    @JsonIgnore
    val password: String,
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss", timezone = "Asia/Seoul")
    val lastLogin: LocalDateTime,
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss", timezone = "Asia/Seoul")
    val dateJoined: LocalDateTime,
    val participant: Participant? = null,
    val instructor: Instructor? = null,
) {
    enum class Role {
        PARTICIPANT, INSTRUCTOR
    }
}